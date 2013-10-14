package de.mensa.sh.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
