package Model;

import java.util.Random;

public class Enemy {

	Random random = new Random();

	// Fields for Enemies
	private String name;
	private int maxHitPoints;
	private int hitPoints;
	private int minDamage;
	private int maxDamage;
	// Current text for appending the gui
	public String currentText = "";
	// Constructor
	public Enemy(String name, int maxHitPoints, int hitPoints, int minDamage, int maxDamage) {
		this.name = name;
		this.maxHitPoints = maxHitPoints;
		this.hitPoints = hitPoints;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
	}
	
	/**
	 * @return The name of the enemy.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method sets the name of the enemy.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return The health of the current enemy.
	 */
	public String getHealth() {
		return "HP: " + hitPoints;
	}

	/**
	 * 
	 * @return The max health of the enemy.
	 */
	public int getMaxHealth() {
		return maxHitPoints;
	}

	/**
	 * Method calculates the damage of the player and usesit to determine the damage caused to the current enemy.
	 * @param player parsed in player object.
	 */
	public void defend(Player player) {
		int attackStrength = player.attack();
		hitPoints = (hitPoints > attackStrength) ? hitPoints - attackStrength : 0;
		setCurrentText("\n" + name + " is hit for " + attackStrength + " damage");
	}

	/**
	 * All method below are based on the original defend method but react to spells damage instead.
	 * @param player parsed in player object.
	 */
	public void defendsp1(Player player) {
		int attackStrength = player.getSpell1();
		hitPoints = (hitPoints > attackStrength) ? hitPoints - attackStrength : 0;
		setCurrentText("\n" + name + " is hit for " + attackStrength + " damage");
	}

	public void defendsp2(Player player) {
		int attackStrength = player.getSpell2();
		hitPoints = (hitPoints > attackStrength) ? hitPoints - attackStrength : 0;
		setCurrentText("\n" + name + " is hit for " + attackStrength + " damage");
	}

	public void defendsp3(Player player) {
		int attackStrength = player.getSpell3();
		hitPoints = (hitPoints > attackStrength) ? hitPoints - attackStrength : 0;
		setCurrentText("\n" + name + " is hit for " + attackStrength + " damage");
	}

	public void defendsp4(Player player) {
		int attackStrength = player.getSpell4();
		hitPoints = (hitPoints > attackStrength) ? hitPoints - attackStrength : 0;
		setCurrentText("\n" + name + " is hit for " + attackStrength + " damage");
	}

	public void defendsp5(Player player) {
		int attackStrength = player.getSpell5();
		hitPoints = (hitPoints > attackStrength) ? hitPoints - attackStrength : 0;
		setCurrentText("\n" + name + " is hit for " + attackStrength + " damage");
	}

	/**
	 * 
	 * @return The current text variables content (string)
	 */
	public String getCurrentText() {
		return currentText;
	}

	/**
	 * Method sets the current text variables contents (string)
	 * @param currentText
	 */
	public void setCurrentText(String currentText) {
		this.currentText = currentText;
	}

	/**
	 * 
	 * @return Returns true if enemy alive, false else.
	 */
	public boolean isAlive() {
		return hitPoints > 0;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * 
	 * @return The attack damage of the current enemy.
	 */
	public int attack() {
		return random.nextInt(maxDamage - minDamage + 1) + minDamage;
	}
	
	public int getMaxHitPoints() {
		return maxHitPoints;
	}

	public void setMaxHitPoints(int maxHitPoints) {
		this.maxHitPoints = maxHitPoints;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	/**
	 * Method sets the minimum damage of the enemy (integer)
	 * @param minDamage
	 */
	public void setMinDamage(int minDamage) {
		this.minDamage = minDamage;
	}

	/**
	 * 
	 * @return Returns the current enemies max damage (integer)
	 */
	public int getMaxDamage() {
		return maxDamage;
	}
	
	/**
	 * 
	 * @return returns the minimum damage of the current enemy (integer)
	 */
	public int getMinDamage() {
		return minDamage;
	}

	/**
	 * Method sets the max damage of the current enemy (integer)
	 * @param maxDamage parsed in max damage integer
	 */
	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}
}