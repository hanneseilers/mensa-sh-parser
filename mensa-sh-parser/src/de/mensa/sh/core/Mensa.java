package de.mensa.sh.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
							Element elementMenue = e.select( "p > a[href]" ).last();
							String mensaMenueURL = "";
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
	
}
