package Controller;

import java.util.ArrayList;
import java.util.Random;

import Model.Enemy;
import Model.Player;

public class EnemyController {

	// Array List of Enemies
	ArrayList<Enemy> npcs = new ArrayList<Enemy>();
	// Current Text of Enemy
	public String currentText = "";

	// Default Constructor
	public EnemyController() {
	}

	/**
	 * Method is designed to pass the array list of enemies into the games controller.
	 * @return An Array List of Enemies
	 */
	public ArrayList<Enemy> getNpcs() {
		return npcs;
	}
	
	/**
	 * Method removes enemy from the array list of enemies.
	 * @param player Takes player from model.
	 */
	public void npcKilled(Player player) {
		npcs.remove(0);
	}

	/**
	 *  Method checks size of array list of enemies.
	 * @return the size of enemy array list.
	 */
	public int npcAlive() {
		return npcs.size();
	}

	/**
	 * Method generates a random enemy and adds it to the array.
	 * Uses random to select individual enemies.
	 * Enemies instantiate with prefixed default values.
	 */
	public void gen() {
		Random randomCount = new Random();
		int randomMonsterCount = randomCount.nextInt(5 - 3) + 3;
		for (int count = 0; count < randomMonsterCount; count++) {
			Random rand = new Random();
			int randomMonster = rand.nextInt(4);
			if (randomMonster == 0) {
				npcs.add(new Enemy("Warrior", 0, 0, 0, 0));
			}
			if (randomMonster == 1) {
				npcs.add(new Enemy("Mage", 0, 0, 0, 0));
			}
			if (randomMonster == 2) {
				npcs.add(new Enemy("Priest", 0, 0, 0, 0));
			}
			if (randomMonster == 3) {
				npcs.add(new Enemy("Archer", 0, 0, 0, 0));
			}
			if (randomMonster == 4) {
				npcs.add(new Enemy("Skeleton", 0, 0, 0, 0));
			}
		}
	}

	/**
	 * Method generates the boss for the game similar to gen().
	 * Adds boss enemy to the array list of enemies.
	 * Prefixed with default values.
	 */
	public void genBoss() {
		npcs.add(new Enemy("Verallion", 0, 0, 0, 0));
	}

	/**
	 * Method generates text and stores into currentText.
	 * Current text variable is used in controller to display contextual information.
	 */
	public void encounter() {
		currentText = (" you have encounterd " + npcs.get(0).toString());
	}
}