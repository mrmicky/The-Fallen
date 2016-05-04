package Model;

import java.util.Random;

public class Player {

	// Player attribute Variables
	private String name;
	private String race;

	// Hitpoint variables
	private int maxHitPoints = 100;
	private int hitPoints = 100;
	private int numPotions = 4;
	private int numManaPotions = 2;

	// Melee damage
	private int minDamage = 10;
	private int maxDamage = 15;

	// Spells variables
	private int spell1 = (int) (maxDamage * (115.f / 100.0f));
	private int spell2 = (int) (maxHitPoints * (35.0f / 100.0f));
	private int spell3 = (int) (maxDamage * (135.f / 100.0f));
	private int spell4 = (int) (maxDamage * (35.0f / 100.0f));
	private int spell5 = (int) (maxDamage * (165.f / 100.0f));

	// Spell count variables
	private int spell1Count = 5;
	private int spell2Count = 3;
	private int spell3Count = 2;
	private int spell4Count = 2;
	private int spell5Count = 2;
	private int healingTideMath = (int) (maxHitPoints * (35.0f / 100.0f));

	// Experience System Variables
	private int currentXp = 0;
	private int maxXp = 100;
	private int currentLevel = 1;
	private int previousLevel = 1;
	private int maxLevel = 99;
	private int cash = 0;

	// Used to Append JTA
	private String currentText = "";

	// Random from API to make combat more interesting
	private Random random = new Random();

	// Constructor for Player
	public Player() {
		this.name = name;
		this.race = race;
		this.maxHitPoints = maxHitPoints;
		this.hitPoints = maxHitPoints;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.numPotions = numPotions;
		this.numManaPotions = numManaPotions;
	}

	/**
	 * Method used to return players stats to their original settings.
	 */
	public void resetPlayer() {
		// Player attribute Variables
		name = null;
		race = null;

		// Hitpoint variables
		maxHitPoints = 100;
		hitPoints = 100;
		numPotions = 4;
		numManaPotions = 2;

		// Melee damage
		minDamage = 10;
		maxDamage = 15;

		// Spells variables
		spell1 = 25;
		spell2 = 20;
		spell3 = 15;
		spell4 = (int) (maxDamage * (35.0f / 100.0f));
		spell5 = (int) (maxHitPoints * (35.0f / 100.0f));

		// Spell count variables
		spell1Count = 2;
		spell2Count = 3;
		spell3Count = 5;
		spell4Count = 2;
		spell5Count = 2;
		healingTideMath = (int) (maxHitPoints * (35.0f / 100.0f));

		currentXp = 0;
		maxXp = 100;
		currentLevel = 1;
		previousLevel = 1;
		maxLevel = 15;
		cash = 0;

		currentText = "";
	}

	// Getters and Setters for variables of Player
	/**
	 * 
	 * @return Gets the players experience points.
	 */
	public int getCurrentXp() {
		return currentXp;
	}

	/**
	 * Sets the users experience points.
	 * @param currentXp
	 */
	public void setCurrentXp(int currentXp) {
		this.currentXp = currentXp;
	}

	/**
	 * 
	 * @return the players maxmimum experience points.
	 */
	public int getMaxXp() {
		return maxXp;
	}

	/**
	 * 
	 * @return Returns the race of the player as a string.
	 */
	public String getRace() {
		return race;
	}

	/**
	 * Method for settings the race of the player.
	 * @param race
	 */
	public void setRace(String race) {
		this.race = race;
	}

	/**
	 * Used for calculations in controller.
	 * @return Returns the players previous level.
	 */
	public int getPreviousLevel() {
		return previousLevel;
	}

	/**
	 * Used to update the players previous Level when necessary.
	 * @param previousLevel
	 */
	public void setPreviousLevel(int previousLevel) {
		this.previousLevel = previousLevel;
	}

	/**
	 * 
	 * @return Returns the players current level as int.
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * Sets the players current level.
	 * @param currentLevel
	 */
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	/**
	 * 
	 * @return Returns the players maximum level.
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * Used throughout the game to append the GUI when necessary.
	 * @return Returns the players current text variable as a string.
	 */
	public String getCurrentText() {
		return currentText;
	}
	
	/**
	 * Methodused to set the players current text variable.
	 * @param currentText
	 */
	public void setCurrentText(String currentText) {
		this.currentText = currentText;
	}

	/**
	 * Method used to set the players name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Returns the players maximum hit points.
	 */
	public int getMaxHitPoints() {
		return maxHitPoints;
	}

	/**
	 * Method allows settings of max hit points.
	 * @param maxHitPoints
	 */
	public void setMaxHitPoints(int maxHitPoints) {
		this.maxHitPoints = maxHitPoints;
	}

	/**
	 * 
	 * @return Returns the current hit points of player.
	 */
	public int getHitPoints() {
		return hitPoints;
	}

	/**
	 * Method used for setting current hit points (duplicate potentially)
	 * @param hitPoints
	 */
	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	/**
	 * 
	 * @return Returns the number of health potions the player has.
	 */
	public int getNumPotions() {
		return numPotions;
	}

	/**
	 * Set method for health potions, used to increment post battle.
	 * @param numPotions
	 */
	public void setNumPotions(int numPotions) {
		this.numPotions = numPotions;
	}

	/**
	 * 
	 * @return Returns the players minimum damage.
	 */
	public int getMinDamage() {
		return minDamage;
	}

	/**
	 * Method sets the players minimum damage.
	 * @param minDamage
	 */
	public void setMinDamage(int minDamage) {
		this.minDamage = minDamage;
	}

	/**
	 * 
	 * @return Returns the players maximum damage.
	 */
	public int getMaxDamage() {
		return maxDamage;
	}

	/**
	 * Sets the players maximum damage.
	 * @param maxDamage
	 */
	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}

	/**
	 * 
	 * @return Returns the players name as a string.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return Returns the players health as a string (duplicate potentially)
	 */
	public String getHealth() {
		return "HP: " + hitPoints;
	}

	/**
	 * 
	 * @return Returns the players maximum health as integer.
	 */
	public int getMaxHealth() {
		return maxHitPoints;
	}

	/**
	 * Old deprecated method for checking if the player is not alive.
	 */
	public void isDead() {
		if (hitPoints <= 0) {
			System.exit(0);
		}
	}

	/**
	 * Method used to check if the player has been named.
	 * @return True if named, false if not.
	 */
	public boolean isNamed() {
		if (!getName().equals(""));
		return false;
	}

	/**
	 * Method updates the amount of experience required each leve by 200%
	 */
	public void incrementExperience() {
		maxXp = maxXp * 2;
	}

	// Attack method, random number between max/min
	/**
	 * Method calculates the players next attack damage.
	 * @return Returns the attack damage as an int.
	 */
	public int attack() {
		return random.nextInt(maxDamage - minDamage + 1) + minDamage;
	}

	/**
	 * All get spell methods return the spell as an int (damage/healing).
	 * @return Spell damage or healing.
	 */
	public int getSpell1() {
		return spell1;
	}

	/**
	 * All set spell methods set the spells damage or healing.
	 * @param spell1
	 */
	public void setSpell1(int spell1) {
		this.spell1 = spell1;
	}

	public int getSpell2() {
		return spell2;
	}

	public void setSpell2(int spell2) {
		this.spell2 = spell2;
	}

	public int getSpell3() {
		return spell3;
	}

	public void setSpell3(int spell3) {
		this.spell3 = spell3;
	}

	public int getSpell4() {
		return spell4;
	}

	public void setSpell4(int spell4) {
		this.spell4 = spell4;
	}

	public int getSpell5() {
		return spell5;
	}

	public void setSpell5(int spell5) {
		this.spell5 = spell5;
	}

	public int getSpell1Count() {
		return spell1Count;
	}

	public void setSpell1Count(int spell1Count) {
		this.spell1Count = spell1Count;
	}

	public int getSpell2Count() {
		return spell2Count;
	}

	public void setSpell2Count(int spell2Count) {
		this.spell2Count = spell2Count;
	}

	public int getSpell3Count() {
		return spell3Count;
	}

	/**
	 * 
	 * @return Returns amount of mana potions player has.
	 */
	public int getNumManaPotions() {
		return numManaPotions;
	}

	/**
	 * Sets number of mana potions the player has.
	 * @param numManaPotions
	 */
	public void setNumManaPotions(int numManaPotions) {
		this.numManaPotions = numManaPotions;
	}

	public void setSpell3Count(int spell3Count) {
		this.spell3Count = spell3Count;
	}

	public int getSpell4Count() {
		return spell4Count;
	}

	public void setSpell4Count(int spell4Count) {
		this.spell4Count = spell4Count;
	}

	public int getSpell5Count() {
		return spell5Count;
	}

	public void setSpell5Count(int spell5Count) {
		this.spell5Count = spell5Count;
	}

	/**
	 * 
	 * @return Returns the amount of cash the player has currently.
	 */
	public int getCash() {
		return cash;
	}

	/**
	 * Method used to update the players cash.
	 * @param cash
	 */
	public void setCash(int cash) {
		this.cash = cash;
	}

	/**
	 * Sets the maxExperience of the player if necessary to reset it.
	 * @param maxXp
	 */
	public void setMaxXp(int maxXp) {
		this.maxXp = maxXp;
	}

	/**
	 * Sets the max level of the player, if necessary to reset.
	 * @param maxLevel
	 */
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	// Defend method (takes npc from npc class), method for fighting between AI
	// and Human
	/**
	 * Method takes in the current enemy within the controller to calculate the damage they do to the current player.
	 * @param npc
	 */
	public void defend(Enemy npc) {
		int attackStrength = npc.attack();
		hitPoints = (hitPoints > attackStrength) ? hitPoints - attackStrength : 0;
		setCurrentText("\n" + name.toString() + " is hit for " + attackStrength + " damage ");
		if (hitPoints <= 0) {
			setCurrentText("\n" + name.toString() + " has been defeated");
		}
	}

	// Method increases players current Helath by set amount. has count
	// (numPotions -1)
	/**
	 * Method used if the player wishes to health potions. Decrements their count for health potion and increases player health.
	 */
	public void heal() {
		if (numPotions > 0) {
			int temp = (int) (maxHitPoints * (15.0f / 100.0f));
			hitPoints += temp;
			if (hitPoints > maxHitPoints) {
				hitPoints = maxHitPoints;
			}
			setCurrentText("\n" + name + " drinks healing potion (" + numPotions + "potions left)");
			numPotions = numPotions - 1;
		} else {
			setCurrentText("\nYou've exhausted your potion supply!");
		}
	}

	public void spell5Cast() {
		hitPoints += spell5;
	}
}