package de.mensa.sh.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
	private String lunchTime = "";
	private List<String> offers = new ArrayList<String>();
	private String menueURL = "";
	private List<Meal> meals = null;
	
	/**
	 * Constructor
	 */
	public Mensa(){	}
	
	/**
	 * Constructor
	 * @param aName
	 * @param aLunchTime
	 * @param aOffers
	 * @param aMenueURL
	 */
	public Mensa(String aCity, String aName, String aLunchTime, List<String> aOffers, String aMenueURL){
		city = aCity;
		name = aName;
		lunchTime = aLunchTime;
		offers = aOffers;
		menueURL = aMenueURL;
	}
	
	/**
	 * @return Available cities with a mensa
	 */
	public static List<String> getCities(){
		List<String> cities = new ArrayList<String>();
		
		try {
			
			Document doc = Jsoup.connect( Settings.sh_mensa_overview ).get();
			Elements htmlElements = doc.select( "td > h1" );
			
			// get locations			
			for( Element e : htmlElements ){
				cities.add( e.text() );
			}
			
		} catch (IOException e) {}
		
		return cities;
	}
	
	/**
	 * Gets all locations of a city 
	 * @param aCity
	 * @return
	 */
	public static List<Mensa> getLocations(String aCity){
		List<Mensa> locations = new ArrayList<Mensa>();
		
		try {
			
			Document doc = Jsoup.connect( Settings.sh_mensa_overview ).get();
			Elements htmlElements = doc.select( "table" ).select( "tr" );
			String baseURL = Settings.sh_mensa_overview.substring( 0,
					Settings.sh_mensa_overview.lastIndexOf('/')+1 );
			
			// find city and locations
			boolean cityFound = false;
			for( Element e : htmlElements ){
				
				// check if city was found
				if( cityFound ){					
					// check if entry corresponds to searched city
					if( e.select("td > h1").size() == 0 ){
						
						// get mensa data
						Element elementName = e.select( "a[href]" ).first();
						if( elementName != null ){							
							// get name
							String mensaName = elementName.text();
							
							// get lunch time
							Element elementLunchTime = e.select( "div" ).first();
							String mensaLunchTime = "";
							if( elementLunchTime != null )
								mensaLunchTime = elementLunchTime.text();
							
							// get offers
							Element elementOffers = e.select( "ul" ).last();
							List<String> mensaOffers = new ArrayList<String>();
							if( elementOffers != null ){								
								for( Element eOffer : elementOffers.select( "li" ))
											mensaOffers.add( eOffer.text() );					
							}
							
							// get menue
							Elements elementsMenue = e.select( "a[href]" );
							String mensaMenueURL = "";
							Element elementMenue = null;
							
							for(Element el : elementsMenue){
								if( el.text().contains("Speiseplan") ){
									elementMenue = el;
									break;
								}
							}
							
							if( elementMenue != null ){
								mensaMenueURL = baseURL + elementMenue.attr("href");
							}
							
							// add new mensa to list
							locations.add( new Mensa(
									aCity,
									mensaName,
									mensaLunchTime,
									mensaOffers,
									mensaMenueURL) );
							
						}						
					}
					else{
						cityFound = false;
					}
					
				}
				
				// check if elements contains city
				if( e.text().contains( aCity ) ){
					cityFound = true;
				}
				
			}
			
		} catch (IOException e) {}
		
		return locations;
	}
	
	/**
	 * @return List of meals of this mensa
	 */
	public List<Meal> getMeals(){
		if(this.meals == null){
			List<Meal> meals = new ArrayList<Meal>();
			MenueLayout layoutMenue = null;
			
			Document doc = Jsoup.parse( getMenueAsHtml() );
			Elements trElements = doc.select( "table" ).select( "tr" );
			
			// get menue layout
			for( Element tr : trElements ){			
				String trText = tr.text().toLowerCase();
				if( trText.contains( "montag ") || trText.contains("mo ") ){
					if( trText.contains("dienstag ") || trText.contains("di ") )
						layoutMenue = MenueLayout.LAYOUT_DAYS_AS_COLS;
					else
						layoutMenue = MenueLayout.LAYOUT_DAYS_AS_ROWS;
				}			
			}
			
			// check if layout found
			if( layoutMenue != null ){
				boolean start = false;
				
				for( Element tr : trElements ){
					
					// COLUMN LAOUT
					if( layoutMenue == MenueLayout.LAYOUT_DAYS_AS_COLS ){
						// check if row contains information about meals
						Elements tdElements = tr.select( "td" );					
						if( tdElements.size() == 5 ){
							
							if(start){
								// iterate over every cell in row
								for( Element td : tdElements ){
									// check if cell contains meal information
									if( !td.text().contains("&euro;")
											&& !td.text().contains("€")
											&& td.text().trim().length() > 2 ){
										
										// add meal to list
										Meal meal = new Meal(td);
										if( !Meal.isMealInList(meals, meal) )
											meals.add( meal );
										
									}
								}
							}
							else{
								start = true;
							}
							
						}
					}
					
					// ROW LAYOUT
					else if( layoutMenue == MenueLayout.LAYOUT_DAYS_AS_ROWS ){
						Elements tdElements = tr.select( "td" );
						String tdElementsTxt = tdElements.text().toLowerCase();
						
						// check if row contains meals
						if( tdElementsTxt.contains("montag ") || tdElementsTxt.contains("mo ")
								|| tdElementsTxt.contains("dienstag ") || tdElementsTxt.contains("di ")
								|| tdElementsTxt.contains("mittwoch ") || tdElementsTxt.contains("mi ")
								|| tdElementsTxt.contains("donnerstag ") || tdElementsTxt.contains("do ")
								|| tdElementsTxt.contains("freitag ") || tdElementsTxt.contains("fr ") ){
							
							// get  meals
							for( Element td : tdElements ){
								String txt = td.text().toLowerCase();
								
								if( !txt.contains("montag ") && !txt.contains("mo ")
										&& !txt.contains("dienstag ") && !txt.contains("di ")
										&& !txt.contains("mittwoch ") && !txt.contains("mi ")
										&& !txt.contains("donnerstag ") && !txt.contains("do ")
										&& !txt.contains("freitag ") && !txt.contains("fr ")
										&& !txt.contains("&euro;") && !txt.contains("€")  ){
									
									// Add meal to list
									Meal meal = new Meal(td);
									if( !Meal.isMealInList(meals, meal) )
										meals.add( meal );
								}
							}
						}
					}
				}
				
			}
			
			this.meals = meals;
		}
		
		return this.meals;
	}
	
	
	/**
	 * @return Menue table as html code
	 */
	public String getMenueAsHtml(){
		String html = "";
		
		// add css files
		html += Settings.getCssLink("allgemein.css");
		html += Settings.getInlineCss();
		
		if( menueURL.trim() != "" ){			
			
			try {
				
				// get menue from url
				Document doc = Jsoup.connect( menueURL ).get();
				
				// remove links to remote sites
				// and unwrap all other links
				doc.select("a[target=_blank]").remove();
				doc.select("a").unwrap();
				Elements tables = doc.select("table");
				
				// add tables html
				for( Element e : tables ){					
					html += e.outerHtml().replace( "src=\"..",
							"src=\""+Settings.sh_mensa_url );					
				}				
				
			} catch (IOException e) {}			
		}
		else {
			// add mensa information
			html += "<b>"+name+"</b><br />";
			html += "Mittagessen: "+lunchTime+"<br />";
			html += "Angebot:<br /><ul>";
			for( String o : offers ){
				html += "<li>"+o+"</li>";
			}
			html += "</ul>";
		}
		
		return html;
	}
	
	
	/**
	 * @param meal
	 * @return rating of meal for this mensa
	 * and -1 if no rating found or error occured
	 */
	public int getRating(Meal meal){		
		try {
			
			// generate request 
			String url = Settings.sh_mensa_meal_db_api_url + "?";
			url += "f=getRating&loc=" + URLEncoder.encode( city, "UTF-8" );
			url += "&mensa=" + URLEncoder.encode( name, "UTF-8" );
			url += "&meal=" + URLEncoder.encode( meal.getMealName(), "UTF-8" );
			url += "&pig=" + bToI( meal.isPig() );
			url += "&cow=" + bToI( meal.isCow() );
			url += "&vege=" + bToI( meal.isVegetarian() );
			url += "&vega=" + bToI( meal.isVegan() );
			url += "&alc=" + bToI ( meal.isAlc() );
					
			// read response from online database
			InputStream in = new URL( url ).openStream();
			String ret = IOUtils.toString( in );
			IOUtils.closeQuietly(in);			
			
			// get rating from response
			if( ret.contains( DatabaseResponses.OK.value ) ){
				String[] data = ret.split( "\\"+DatabaseResponses.SEPERATOR.value );
				if( data.length >= 2 ){
					try{
						return Integer.parseInt(data[1]);
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
		
		return -1;
	}
	
	/**
	 * Adds rating for meal of this mensa to database
	 * @param meal
	 * @param aRating
	 * @return true if successfull
	 */
	public boolean addRating(Meal meal, int aRating, String comment, String hash){
		try {
			
			// generate request 
			String url = Settings.sh_mensa_meal_db_api_url + "?";
			url += "f=addRating&loc=" + URLEncoder.encode( city, "UTF-8" );
			url += "&mensa=" + URLEncoder.encode( name, "UTF-8" );
			url += "&meal=" + URLEncoder.encode( meal.getMealName(), "UTF-8" );
			url += "&pig=" + bToI( meal.isPig() );
			url += "&cow=" + bToI( meal.isCow() );
			url += "&vege=" + bToI( meal.isVegetarian() );
			url += "&vega=" + bToI( meal.isVegan() );
			url += "&alc=" + bToI ( meal.isAlc() );
			url += "&rating=" + Integer.toString( aRating );
			url += "&com=" + URLEncoder.encode( comment, "UTF-8" );
			url += "&hash=" + URLEncoder.encode( hash, "UTF-8" );
					
			// read response from online database
			InputStream in = new URL( url ).openStream();
			String ret = IOUtils.toString( in );
			IOUtils.closeQuietly(in);			
			
			// get rating from response
			if( ret.contains( DatabaseResponses.OK.value ) ){
				return true;
			}
			else{
				System.out.println("ERROR: " + url);
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
			String url = Settings.sh_mensa_meal_db_api_url + "?";
			url += "f=getRating&loc=" + URLEncoder.encode( city, "UTF-8" );
			url += "&mensa=" + URLEncoder.encode( name, "UTF-8" );
			url += "&meal=" + URLEncoder.encode( meal.getMealName(), "UTF-8" );
			url += "&pig=" + bToI( meal.isPig() );
			url += "&cow=" + bToI( meal.isCow() );
			url += "&vege=" + bToI( meal.isVegetarian() );
			url += "&vega=" + bToI( meal.isVegan() );
			url += "&alc=" + bToI ( meal.isAlc() );
					
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
	 * @param string
	 * @return md5 hash of string
	 */
	public static String md5(String string){
		MessageDigest md5;
		try {
			
			md5 = MessageDigest.getInstance("MD5");
			md5.reset();
	        md5.update(string.getBytes());
	        byte[] result = md5.digest();

	        /* Ausgabe */
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<result.length; i++) {
	            hexString.append(Integer.toHexString(0xFF & result[i]));
	        }
	        
	        return hexString.toString();
		} catch (NoSuchAlgorithmException 
				e) {
			e.printStackTrace();
		}
		
		return "";        
	}
	
	
	/**
	 * Prints mensa data
	 */
	public String toString(){
		String rString = "Mensa: "+name+" ("+city+")\n";
		rString += "lunch time: "+lunchTime+"\nOffers:\n";
		
		for( String o : offers ){
			rString += "\t- "+o+"\n";
		}
		
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
		return lunchTime;
	}

	/**
	 * @return the offers
	 */
	public List<String> getOffers() {
		return offers;
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
	 * @param offers the offers to set
	 */
	public void setOffers(List<String> offers) {
		this.offers = offers;
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
	
	
	/**
	 * @param b
	 * @return integer 1 for true and 0 for false of boolean b
	 */
	public static int bToI(boolean b) {
	    return b ? 1 : 0;
	}
	
}
