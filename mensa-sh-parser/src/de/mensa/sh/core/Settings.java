package de.mensa.sh.core;

/**
 * Class with settings
 * @author hannes
 *
 */
public class Settings {

	public static String sh_mensa_url = "http://studentenwerk-s-h.de";
	public static String sh_mensa_overview = "http://studentenwerk-s-h.de/seiten_essen/essen_plaene.html";	
	public static String sh_mensa_css = "http://studentenwerk-s-h.de/css_stile/";
	
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
