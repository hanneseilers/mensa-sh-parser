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

	public static String sh_mensa_url = "http://www.studentenwerk.sh";
	public static String sh_mensa_overview = "/de/essen/standorte/index.html";
	public static String sh_mensa_url_base = "/de/essen/standorte/";
	public static String sh_mensa_url_ending = "index.html";
	public static String sh_mensa_lunch_time_search_string = "ffnungszeiten";
	public static String sh_mensa_meal_url_ending = "speiseplan.html";
	public static String sh_mensa_meal_day_prefix = "day_";
	public static String sh_mensa_css = "http://studentenwerk-s-h.de/css_stile/";
	public static String sh_mensa_db_api_url = "http://localhost/mensa-sh-rating/website/api.php";
	public static String sh_mensa_rating_ico_full_url = "http://localhost/mensa-sh-rating/website/img/star.png";
	public static String sh_mensa_rating_ico_empty_url = "http://localhost/mensa-sh-rating/website/img/star_empty.png";
	public static String sh_mensa_rating_ico_half_url = "http://localhost/mensa-sh-rating/website/img/star_half.png";
	public static String sh_mensa_rating_ico_size = "16px";
	
	public static String sh_mensa_meal_img_url = "http://studentenwerk-sh.eu/cms/css/img/menu/";
	public static String sh_mensa_meal_img_pig = "iconProp_s.hd.png";
	public static String sh_mensa_meal_img_cow = "iconProp_r.hd.png";
	public static String sh_mensa_meal_img_vegetarian = "iconProp_vegetarisch.hd.png";
	public static String sh_mensa_meal_img_vegan = "iconProp_vegan.hd.png";
	public static String sh_mensa_meal_img_alc = "iconProp_a.hd.png";
	
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
