package de.mensa.sh.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class for testing mensa library
 * @author hannes
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// get all available locations
		for( String city : Mensa.getCities() ){
			for( Mensa mensa : Mensa.getLocations(city) ){
				
				// print data
				System.out.println( "------------\n" + mensa );
				System.out.println( "\tnum of meals tis week: " + mensa.getMeals().size() );
				List<Meal> meals = mensa.getMeals();
				if( meals.size() > 0 ){
					Meal meal = meals.get( meals.size()-1 );
					Integer rating = mensa.getRating(meal);
					
					// add rating if not rated jet
					if( rating < 0 ){
						mensa.addRating(meal, 3, "", "hannes");
					}else{
						System.out.println(rating + ": " + meal);
					}
				}
				
				// save menue as html file
				try {
					
					BufferedWriter file = new BufferedWriter(
							new FileWriter( mensa.getCity()+"_"+mensa.getName()+".html" ));
					file.write( mensa.getMenueAsHtml() );
					file.flush();
					file.close();
					System.out.println( "menue saved as " + mensa.getName()+".html" );
					
				} catch (IOException e) {}
				
			}
		}
	}

}
