package de.mensa.sh.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Main class for getting mensa information
 * @author hannes
 *
 */
public class Mensa {

	private String city = "";
	private String name = "";
	private String lunchTime = null;
	private String mensaURL = "";
	private String menueURL = "";
	private List<Meal> meals = null;
	private Hashtable<String, Integer> ratings = new Hashtable<String, Integer>(); 
	
	public final static String skipClassNames = "mitteilung";
	public final static String serialSeperator = "###"; 
	public final static String serialListSeperator = "##";
	public final static int serialElements = 4;
	
	/**
	 * Constructor
	 */
	public Mensa(){	}
	
	/**
	 * Constructor
	 * @param aCity
	 * @param aName
	 * @param aLunchTime
	 * @param aMenueURL
	 */
	public Mensa(String aCity, String aName, String aMensaURL, String aMenueURL){
		setCity(aCity);
		setName(aName);
		setMensaURL(aMensaURL);
		setMenueURL(aMenueURL);
	}

	/**
	 * @return Available cities with a mensa
	 */
	public static List<String> getCities(){
		List<String> cities = new ArrayList<String>();

		try {

			Document doc = Jsoup.connect( Settings.sh_mensa_url + Settings.sh_mensa_overview ).get();
			Elements htmlElements = doc.select( "#menuCitySelect option:not([value=0])" );

			// get locations			
			for( Element e : htmlElements ){
				cities.add( e.text() );
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return cities;
	}

	/**
	 * Gets all locations.
	 * @return {@link List} of {@link Mensa} locations.
	 */
	public static List<Mensa> getLocations(){
		List<Mensa> locations = new ArrayList<Mensa>();

		// get document and elements
		Document doc = Cache.getDocument( Settings.sh_mensa_url + Settings.sh_mensa_overview );
		if( doc != null ){
			Elements htmlElements = doc.select( ".menuMensaSelect option[value]" );
				
			for( Element e : htmlElements ){
				
				// extract features
				String mensaCity = e.attr("value").replace(Settings.sh_mensa_url_base, "").split("/")[0];
				mensaCity = Character.toUpperCase( mensaCity.charAt(0) ) + mensaCity.substring(1);
				
				String mensaUrl = Settings.sh_mensa_url	+ e.attr("value");
				String mensaMenueUrl = mensaUrl.replace(Settings.sh_mensa_url_ending, Settings.sh_mensa_meal_url_ending);
				String mensaName = e.text();
				
				// add new mensa
				locations.add( new Mensa(
						mensaCity,
						mensaName,
						mensaUrl,
						mensaMenueUrl) );
			}
		}

		return locations;
	}

	/**
	 * Checks if text contains price
	 * @param text
	 * @return
	 */
	public static boolean containsPrice(String text){
		if( text.contains("&euro;")
				|| text.contains("\u20ac")
				|| text.contains("€")
				|| text.contains("EUR") )
			return true;
		return false;
	}
	
	/**
	 * @return List of meals of this mensa
	 */
	public List<Meal> getMeals(){
		if(meals == null){
			
			List<Meal> vMeals = new ArrayList<Meal>();
			Document doc = Cache.getDocument(  getMenueURL() );
			
			if( doc != null ){
				Elements menus = doc.select("#days");
				
				// go thorugh all weeks and all days
				for( Element vWeek : menus ){		
					for( Element vDay : vWeek.select(".day") ){
						
						for( Element vMeal : vDay.select(".odd") ){						
							try{								
								int vMealDay = Integer.parseInt( vDay.attr("id").replace(Settings.sh_mensa_meal_day_prefix, "") );
								vMeals.add( new Meal(vMeal, vMealDay) );								
							} catch(NumberFormatException e){}
						}
						
						for( Element vMeal : vDay.select(".even") ){						
							try{								
								int vMealDay = Integer.parseInt( vDay.attr("id").replace(Settings.sh_mensa_meal_day_prefix, "") );
								vMeals.add( new Meal(vMeal, vMealDay) );								
							} catch(NumberFormatException e){}
						}
						
					}
				}
				
				meals = vMeals;
			}
			
		}
		
		return meals;
	}
	
	/**
	 * @param meal
	 * @return rating of meal for this mensa
	 * and -1 if no rating found or error occured.
	 * Rating is only updated online once
	 */
	public int getRating(Meal meal){
		return getRating(meal, false);
	}
	/**
	 * @param meal
	 * @paramn update If true the rating is updated online
	 * @return rating of meal for this mensa
	 * and -1 if no rating found or error occured
	 */
	public int getRating(Meal meal, boolean update){
		int rating = -1;
		
		if( ratings.containsKey(meal.getKey()) && !update ){
			
			// chached rating
			rating = ratings.get(meal.getKey());
			
		}
		else{
			
			// rating not cached
			try {
				
				// generate request 
				String url = Settings.sh_mensa_db_api_url + "?";
				url += "f=getRating" + URLBuilder.buildURLParameter(this, meal);
						
				// read response from online database
				InputStream in = new URL( url ).openStream();
				String ret = IOUtils.toString( in );
				IOUtils.closeQuietly(in);			
				
				// get rating from response
				if( ret.contains( DatabaseResponses.OK.value ) ){
					String[] data = ret.split( "\\"+DatabaseResponses.SEPERATOR.value );
					if( data.length > 1 ){
						try{
							rating = Integer.parseInt(data[1]);
						} catch(Exception e) {}
					}
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return rating;
	}
	
	/**
	 * @param meals List of meals
	 * @return Hashtable with meals key as identifier and rating for each meal in meal list.
	 */
	public Hashtable<String, Integer> getRatings(List<Meal> meals){
		Hashtable<String, Integer> ratings = new Hashtable<String, Integer>();
		
		try {			
			String url = Settings.sh_mensa_db_api_url + "?" + "f=getRatingQuery" + URLBuilder.buildURLParameter(this, meals);
		
			// read response from online database
			InputStream in = new URL( url ).openStream();
			BufferedReader reader = new BufferedReader( new InputStreamReader(in) );		
			String line = reader.readLine();
			
			if( line != null && line.contains(DatabaseResponses.OK.value) ){		
				
					while( (line=reader.readLine()) != null ){
						String[] data = line.split( "\\"+DatabaseResponses.SEPERATOR.value );
						if( data.length > 1 ){
								if( !data[1].equals(DatabaseResponses.NOT_FOUND.value) ){
									ratings.put(data[0], Integer.parseInt(data[1]));
								}
						}
					}
				
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ratings;		
	}

	/**
	 * Adds rating for meal of this mensa to database
	 * @param meal
	 * @param rating
	 * @param hash
	 * @return true if successfull
	 */
	public boolean addRating(Meal meal, int rating, String comment, String hash){
		try {

			// generate request 
			String url = Settings.sh_mensa_db_api_url + "?";
			url += "f=addRating" + URLBuilder.buildURLParameter(this, meal, rating, comment, hash);
			
			// read response from online database
			InputStream in = new URL( url ).openStream();
			String ret = IOUtils.toString( in );
			IOUtils.closeQuietly(in);

			// get rating from response
			if( ret.contains( DatabaseResponses.OK.value ) ){
				return true;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * @param meal
	 * @return List of comments of a meal
	 */
	public List<String> getComments(Meal meal){
		List<String> comments = new ArrayList<String>();

		try {

			// generate request 
			String url = Settings.sh_mensa_db_api_url + "?";
			url += "f=getRating" + URLBuilder.buildURLParameter(this, meal);
			// read response from online database
			InputStream in = new URL( url ).openStream();
			String ret = IOUtils.toString( in );
			IOUtils.closeQuietly(in);			

			// get rating from response
			if( ret.contains( DatabaseResponses.OK.value ) ){
				String[] data = ret.split( DatabaseResponses.SEPERATOR.value );
				if( data.length >= 2 ){
					for( int i=1; i < data.length; i++ ){
						String com = data[i].trim();
						if( com.length() > 1 )
							comments.add( com );
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return comments;
	}
	
	/**
	 * Prints mensa data
	 */
	public String toString(){
		String rString = "Mensa: "+ getName() +" ("+ getCity() +")\n";
		rString += "lunch time: "+ getLunchTime() +"\n";

		rString += "menue url: "+menueURL;

		return rString;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the lunchTime
	 */
	public String getLunchTime() {
		if( lunchTime == null ){				
			Document doc = Cache.getDocument( getMensaURL() );
			
			if( doc != null ){
				Element vElement = doc.select( ".htmlcontent:contains("
						+ Settings.sh_mensa_lunch_time_search_string + ")" ).last();
				lunchTime = vElement.text();
			}
		}
		
		return lunchTime;
	}
	
	/**
	 * @return	the mensaURL
	 */
	public String getMensaURL(){
		return mensaURL;
	}

	/**
	 * @return the menueURL
	 */
	public String getMenueURL() {
		return menueURL;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param lunchTime the lunchTime to set
	 */
	public void setLunchTime(String lunchTime) {
		this.lunchTime = lunchTime;
	}
	
	/**
	 * @param mensaURL	the mensaURL to set
	 */
	public void setMensaURL(String mensaURL){
		this.mensaURL = mensaURL;
	}

	/**
	 * @param menueURL the menueURL to set
	 */
	public void setMenueURL(String menueURL) {
		this.menueURL = menueURL;
	}

	/**
	 * @param meals the meals to set
	 */
	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}
	
}
