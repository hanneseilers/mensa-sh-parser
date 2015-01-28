package de.mensa.sh.core;

import java.io.UnsupportedEncodingException;
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
	private int rating = 0;
	private int day = -1;
	public final static int serialElements = 8;
	
	/**
	 * Constructor
	 */
	public Meal() {}
	
	/**
	 * Constructor
	 * @param name
	 */
	public Meal(String name) {
		this();
		mealName = name;
	}
	
	/**
	 * Constructor
	 * @param aMealElement	{@link Element} of meal.
	 * @param aMealDay		{@link Integer} of meals day number.
	 */
	public Meal(Element aMealElement, int aMealDay) {
		this();
		setParameter(aMealElement);
		setDay(aMealDay);
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
		setPrice(price);
		setDay(day);
	}
	
	/**
	 * @return Unique key for this meal
	 */
	public String getKey(){
		String key = URLBuilder.convertStringMutations(getMealName()).replace(" ", "_");
		key += isPig() ? "1" : "0";
		key += isCow() ? "1" : "0";
		key += isVegetarian() ? "1" : "0";
		key += isVegan() ? "1" : "0";
		key += isAlc() ? "1" : "0";
		return key;
	}
	
	/**
	 * Sets parameters from meals html {@link Element}.
	 * @param aMealElement	{@link Element} of meal.
	 */
	private void setParameter(Element aMealElement){
		setMealName( aMealElement.select("td strong").first().text() );
		
		Elements imgElements = aMealElement.select(".properties img");		
		String sPig = Settings.sh_mensa_meal_img_pig;
		String sCow = Settings.sh_mensa_meal_img_cow;
		String sVegetarian = Settings.sh_mensa_meal_img_vegetarian;
		String sVegan = Settings.sh_mensa_meal_img_vegan;
		String sAlc = Settings.sh_mensa_meal_img_alc;
		
		for( Element img: imgElements ){
			String src = img.attr("src");
			if( src.contains(sPig) )
				setPig(true);
			else if( src.contains(sCow) )
				setCow(true);
			else if( src.contains(sVegetarian) )
				setVegetarian(true);
			else if( src.contains(sVegan) )
				setVegan(true);
			else if( src.contains(sAlc) )
				setAlc(true);
		}
			
		setPrice( aMealElement.select("td").last().text() );		
		
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
	 * @param day the day to set
	 */
	public void setDay(int day){
		this.day = day;
	}
	
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	
	/**
	 * @param price	the price to set
	 */
	public void setPrice(String price){
		this.price = price;
	}
	
	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return rating the rating to set
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @return String of serialized object
	 */
	public static String serialize(Meal meal){
		String serializedObject;
		try {

			serializedObject = Mensa.serialSeperator + URLBuilder.convertStringMutations(meal.getMealName());
			serializedObject += Mensa.serialSeperator + URLBuilder.convertStringMutations(meal.getPrice());
			serializedObject += Mensa.serialSeperator + meal.isAlc();
			serializedObject += Mensa.serialSeperator + meal.isCow();
			serializedObject += Mensa.serialSeperator + meal.isPig();
			serializedObject += Mensa.serialSeperator + meal.isVegan();
			serializedObject += Mensa.serialSeperator + meal.isVegetarian();
			serializedObject += Mensa.serialSeperator + meal.getDay();

			return URLBuilder.encode(serializedObject);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * @param serializedObject
	 * @return Meal from serialzed object string
	 */
	public static Meal unserialize(String serializedObject){
		try {

			serializedObject = URLBuilder.decode(serializedObject);		
			String[] objectArray = serializedObject.split( Mensa.serialSeperator );

			int offset = -1;			
			if( objectArray.length == serialElements  ){				
				// only meal data in string			
				offset = 0;
			}
			else if( objectArray.length == Mensa.serialElements + serialElements ){				
				// mensa and meal data in string
				offset = Mensa.serialElements;
			}

			if( offset > -1 ){
				Meal meal = new Meal( objectArray[offset+1],
							Boolean.parseBoolean( objectArray[offset+5] ),							
							Boolean.parseBoolean( objectArray[offset+4] ),
							Boolean.parseBoolean( objectArray[offset+7] ),
							Boolean.parseBoolean( objectArray[offset+6] ),
							Boolean.parseBoolean( objectArray[offset+3] ),
							objectArray[offset+2],
							Integer.parseInt( objectArray[offset+8] ) );
				return meal;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
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
