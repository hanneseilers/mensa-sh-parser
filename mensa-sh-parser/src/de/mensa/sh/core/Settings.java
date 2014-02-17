package de.mensa.sh.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class with settings
 * @author hannes
 *
 */
public class Settings {

	public static String sh_mensa_url = "http://studentenwerk-s-h.de";
	public static String sh_mensa_overview = "http://studentenwerk-s-h.de/seiten_essen/essen_plaene.html";	
	public static String sh_mensa_css = "http://studentenwerk-s-h.de/css_stile/";
	public static String sh_mensa_db_api_url = "http://localhost/mensa-sh-rating/website/api.php";
	public static String sh_mensa_rating_ico_full_url = "http://localhost/mensa-sh-rating/website/img/star.png";
	public static String sh_mensa_rating_ico_empty_url = "http://localhost/mensa-sh-rating/website/img/star_empty.png";
	public static String sh_mensa_rating_ico_half_url = "http://localhost/mensa-sh-rating/website/img/star_half.png";
	public static String sh_mensa_rating_ico_size = "16px";
	
	public static String sh_mensa_meal_img_url = "http://studentenwerk-s-h.de/bilder/bilder_essen/";
	public static String sh_mensa_meal_img_pig = "logo_schwein.png";
	public static String sh_mensa_meal_img_cow = "logo_rind.png";
	public static String sh_mensa_meal_img_vegetarian = "logo_vegetarisch.png";
	public static String sh_mensa_meal_img_vegan = "logo_vegan.png";
	public static String sh_mensa_meal_img_alc = "logo_alkohol.png";
	
	/**
	 * @param cssFile
	 * @param baseURL
	 * @return html link of css file with a specific base url
	 */
	public static String getCssLink(String baseURL, String cssFile){
		return "<link href=\"" + sh_mensa_css + cssFile
				+ "\" rel=\"stylesheet\" type=\"text/css\" />\n";
	}
	
	/**
	 * @param cssFile
	 * @return html link of css file with basic base url
	 */
	public static String getCssLink(String cssFile){
		return getCssLink(sh_mensa_css, cssFile);
	}
	
	/**
	 * @return inline css for replacing content of css files
	 */
	public static String getInlineCss(){
		try {
			
			String css = "<style type=\"text/css\">";
			BufferedReader in = new BufferedReader(
					new InputStreamReader( Settings.class.getResourceAsStream("inline.css") ) );
			String line;
		
			while( (line=in.readLine()) != null ){
				css += line + "\n";
			}
			
			css += "</style>";
			return css;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
}
