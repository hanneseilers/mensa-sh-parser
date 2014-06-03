package de.mensa.sh.core;

/**
 * Class with settings
 * @author hannes
 *
 */
public class Settings {

	public static String sh_mensa_url = "http://studentenwerk-sh.eu/";
	public static String sh_mensa_overview = "http://studentenwerk-sh.eu/essen/standorte/index.html";	
	public static String sh_mensa_css = "http://studentenwerk-s-h.de/css_stile/";
	public static String sh_mensa_meal_db_api_url = "http://192.168.0.106/mensash/api.php";
	
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
		return "<style type=\"text/css\">body{"
				+ "background-image: none;"
				+ "background-color: #eeeeee;"
				+ "}"
				+ "</style>";
	}
	
}
