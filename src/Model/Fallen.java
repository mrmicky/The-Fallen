package Model;

public class Fallen {
	
	private String name;
	private int level;
	private String race;
	@SuppressWarnings("unused")
	private String levelString;
	private String whereFell;

	/**
	 * 
	 * @return Returns a string containing information on fallen character.
	 */
	public String getFallen() {
		return name + level + whereFell;
	}
	
	/**
	 * 
	 * @return gets the name of the fallen character
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method sets the name of the fallen character.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return gets the race of the fallen character.
	 */
	public String getRace() {
		return race;
	}

	/**
	 * Methods sets the race of the fallen character.
	 * @param race
	 */
	public void setRace(String race) {
		this.race = race;
	}

	/**
	 * 
	 * @return returns the level of the fallen character
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * 
	 * @return returns the level of the fallen character as an integer.
	 */
	public String parseString() {
		return levelString = String.valueOf(level);
	}

	/**
	 * Sets the level of the fallen character.
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 
	 * @return Returns the place where the character became "Fallen".
	 */
	public String getWhereFell() {
		return whereFell;
	}

	/**
	 * Method sets the last known location of the character before becomming "Falen".
	 * @param whereFell
	 */
	public void setWhereFell(String whereFell) {
		this.whereFell = whereFell;
	}
}
