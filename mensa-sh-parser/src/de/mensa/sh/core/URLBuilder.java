package de.mensa.sh.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Builds urls
 * @author Hannes Eilers *
 */
public final class URLBuilder {
	
	private static final String encoding = "UTF-8";

	/**
	 * Builds parameters string for url
	 * @param mensa
	 * @param meal
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String buildURLParameter(Mensa mensa, Meal meal) throws UnsupportedEncodingException{
		String url = "&loc=" + URLEncoder.encode( convertStringMutations(mensa.getCity()), encoding );
		url += "&mensa=" + URLEncoder.encode( convertStringMutations(mensa.getName()), encoding );
		url += "&meal=" + URLEncoder.encode( convertStringMutations(meal.getMealName()), encoding );
		url += "&pig=" + bToI( meal.isPig() );
		url += "&cow=" + bToI( meal.isCow() );
		url += "&vege=" + bToI( meal.isVegetarian() );
		url += "&vega=" + bToI( meal.isVegan() );
		url += "&alc=" + bToI ( meal.isAlc() );
		return url;
	}
	
	/**
	 * Builds parameters string for url
	 * @param mensa
	 * @param meal
	 * @param rating
	 * @param comment
	 * @param hash
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String buildURLParameter(Mensa mensa, Meal meal, int rating, String comment, String hash) throws UnsupportedEncodingException{
		String url = "&loc=" + URLEncoder.encode( convertStringMutations(mensa.getCity()), encoding );
		url += "&mensa=" + URLEncoder.encode( convertStringMutations(mensa.getName()), encoding );
		url += "&meal=" + URLEncoder.encode( convertStringMutations(meal.getMealName()), encoding );
		url += "&pig=" + bToI( meal.isPig() );
		url += "&cow=" + bToI( meal.isCow() );
		url += "&vege=" + bToI( meal.isVegetarian() );
		url += "&vega=" + bToI( meal.isVegan() );
		url += "&alc=" + bToI ( meal.isAlc() );
		url += "&rating=" + Integer.toString( rating );
		url += "&com=" + URLEncoder.encode( md5(convertStringMutations(comment)), encoding );
		url += "&hash=" + URLEncoder.encode( convertStringMutations(hash), encoding );
		return url;
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
	 * Replaces mutations in string
	 * @param s
	 * @return 
	 */
	private static String convertStringMutations(String s){
		s = s.replace("ä", "ae");
		s = s.replace("ö", "oe");
		s = s.replace("ü", "ue");
		s = s.replace("Ä", "Ae");
		s = s.replace("Ö", "Oe");
		s = s.replace("Ü", "Ue");
		s = s.replace("ß", "ss");
		return s;
	}
	
	/**
	 * @param b
	 * @return integer 1 for true and 0 for false of boolean b
	 */
	private static int bToI(boolean b) {
	    return b ? 1 : 0;
	}
	
}
