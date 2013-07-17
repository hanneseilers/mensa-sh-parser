package de.mensa.sh.core;

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
		for( String city : Mensa.getCities() ){
			for( Mensa mensa : Mensa.getLocations(city) ){
				System.out.println( "------------\n" + mensa );
			}
		}
	}

}
