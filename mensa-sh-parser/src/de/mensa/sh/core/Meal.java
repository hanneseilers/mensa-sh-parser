package de.mensa.sh.core;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Meal {
	
	private String mealName = "";
	private boolean pig = false;
	private boolean cow = false;
	private boolean vegetarian = false;
	private boolean vegan = false;
	private boolean alc = false;
	private String price = "";
	private String date = "";
	private int day = 0;
	
	/**
	 * Constructor
	 */
	public Meal() {
	}
	
	/**
	 * Constructor
	 * @param name
	 */
	public Meal(String name) {
		this();
		setMealName(name);
	}
	
	public Meal(Element mealHtmlElement, Element priceHtmlElement, int day){
		this(mealHtmlElement.text());
		setDay(day);
		setParameter(mealHtmlElement, priceHtmlElement);
	}
	
	public Meal(Element mealHtmlElement, Element priceHtmlElement, String date, int day){
		this(mealHtmlElement.text());
		setDay(day);
		setDate(date);
		setParameter(mealHtmlElement, priceHtmlElement);
	}
	
	public Meal(Element mealHtmlElement, int day){
		this(mealHtmlElement.text());
		setDay(day);
		setParameter(mealHtmlElement, null);
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param pig
	 * @param cow
	 * @param vegetarian
	 * @param vegan
	 * @param alc
	 */
	public Meal(String name, boolean pig, boolean cow, boolean vegetarian, boolean vegan, boolean alc, String price, int day){
		setMealName(name);
		setPig(pig);
		setCow(cow);
		setVegetarian(vegetarian);
		setVegan(vegan);
		setAlc(alc);
		setPrice(price);
		setDay(day);
	}
	
	/**
	 * Sets parameters from html element
	 * @param htmlText
	 */
	private void setParameter(Element htmlElement, Element priceElement){
		Elements imgElements = htmlElement.select("img");
		String url = Settings.sh_mensa_meal_img_url;
		String sPig = url + Settings.sh_mensa_meal_img_pig;
		String sCow = url + Settings.sh_mensa_meal_img_cow;
		String sVegetarian = url + Settings.sh_mensa_meal_img_vegetarian;
		String sVegan = url + Settings.sh_mensa_meal_img_vegan;
		String sAlc = url + Settings.sh_mensa_meal_img_alc;
		
		for( Element img: imgElements ){
			String src = img.attr("src");
			if( src.contains(sPig) )
				pig = true;
			else if( src.contains(sCow) )
				cow = true;
			else if( src.contains(sVegetarian) )
				vegetarian = true;
			else if( src.contains(sVegan) )
				vegan = true;
			else if( src.contains(sAlc) )
				alc = true;
		}
		
		if (priceElement != null){		
			price = priceElement.text();
		}
		
		
	}
	
	
	/**
	 * Generates string of meal
	 */
	public String toString(){
		String rString = "Meal: " + mealName;
		rString += "\n\t[pig:" + pig + " cow:" + cow + " vegetarian:"
				+ vegetarian + " vegan:" + vegan + " alc:" + alc + "]"; 
		return rString;
	}

	/**
	 * @return the mealName
	 */
	public String getMealName() {
		return mealName;
	}

	/**
	 * @param mealName the mealName to set
	 */
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	/**
	 * @return the pig
	 */
	public boolean isPig() {
		return pig;
	}

	/**
	 * @param pig the pig to set
	 */
	public void setPig(boolean pig) {
		this.pig = pig;
	}

	/**
	 * @return the cow
	 */
	public boolean isCow() {
		return cow;
	}

	/**
	 * @param cow the cow to set
	 */
	public void setCow(boolean cow) {
		this.cow = cow;
	}

	/**
	 * @return the vegetarian
	 */
	public boolean isVegetarian() {
		return vegetarian;
	}

	/**
	 * @param vegetarian the vegetarian to set
	 */
	public void setVegetarian(boolean vegetarian) {
		this.vegetarian = vegetarian;
	}

	/**
	 * @return the vegan
	 */
	public boolean isVegan() {
		return vegan;
	}

	/**
	 * @param vegan the vegan to set
	 */
	public void setVegan(boolean vegan) {
		this.vegan = vegan;
	}

	/**
	 * @return the alc
	 */
	public boolean isAlc() {
		return alc;
	}

	/**
	 * @param alc the alc to set
	 */
	public void setAlc(boolean alc) {
		this.alc = alc;
	}
	
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	private void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @param date the date to set
	 */
	private void setDate(String date) {
		this.date = date;
	}

	/**
	 * @param day the day to set
	 */
	private void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * @param mealList
	 * @param meal
	 * @return true if meal is in mealList
	 */
	public static boolean isMealInList(List<Meal> mealList, Meal meal){
		for( Meal m : mealList ){
			if( m.getMealName().equals(meal.getMealName()) )
				return true;
		}
		return false;
	}

}
