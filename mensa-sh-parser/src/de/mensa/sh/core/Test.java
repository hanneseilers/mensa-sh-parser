package de.mensa.sh.core;

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
		
		long t1 = System.currentTimeMillis();
		List<Mensa> mensas = Mensa.getLocations();
		long t2 = System.currentTimeMillis();
		System.out.println("Get locations in " + (t2-t1)/1000f + "s.");
		
		// get all available locations
		for( Mensa mensa : mensas ){
			
			// Get list of meals
			t1 = System.currentTimeMillis();
			List<Meal> meals = mensa.getMeals();
			t2 = System.currentTimeMillis();
			
			// Print some data about the mensa
			System.out.println( "------------\n" + mensa );
			System.out.println("Get meals in " + (t2-t1)/1000f + "s.");
			System.out.println( "\tnum of meals tis week: " + meals.size() );				
			
			/*
			 * Adding rating to all unrated meals using
			 * independent request to database for ervery meal.
			 */
			if( enableRatings ){
				for( Meal meal : meals ){
					Integer rating = mensa.getRating(meal);
					
					// add rating if not rated jet
					if( rating < 100 ){
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
			
		}

	}

}
