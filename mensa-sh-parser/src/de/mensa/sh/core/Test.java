package de.mensa.sh.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
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
		
		boolean enableRatings = false;
		
		// get all available locations
		for( String city : Mensa.getCities() ){
			for( Mensa mensa : Mensa.getLocations(city) ){
				
				if( mensa.getName().contains("Wedel") ){
					continue;
				}
				
				// Get list of meals
				List<Meal> meals = mensa.getMeals();
				
				// Print some data about the mensa
				System.out.println( "------------\n" + mensa );
				System.out.println( "\tnum of meals tis week: " + meals.size() );				
				
				/*
				 * Adding rating to all unrated meals using
				 * independent request to database for ervery meal.
				 */
				if( enableRatings ){
					for( Meal meal : meals ){
						Integer rating = mensa.getRating(meal);
						
						// add rating if not rated jet
						if( rating < 0 ){
							mensa.addRating(meal, (int) Math.round(Math.random() * 5), "", "test");
						}
					}
				}
				
				/*
				 * Print meals ratings using a queried call to
				 * get all ratings with one request to database.
				 */
				if( enableRatings ){
					Hashtable<String, Integer> ratings = mensa.getRatings(meals);
					for( Meal meal : meals ){
						String key = meal.getKey();
						int rating = -1;
						if( ratings.containsKey(key) ){
							rating = ratings.get(key);						
						}
						System.out.println("\t* " + rating + ": " + meal.getMealName());
					}
				}
				
				/*
				 * Print meals without ratings
				 */
				if( !enableRatings ){
					for( Meal meal: meals ){
						System.out.println( "\t* " + meal.getDay() + ": " + meal.getMealName() + "[" + meal.getPrice() + "]" );
					}
				}
				
				// Save menue as html file
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
