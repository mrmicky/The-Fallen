package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Controller.EnemyController;
import sounds.Sound;

public class Dungeon {
	// Dungeon instantiates an EnemyController (for calling a list of enemies)
	EnemyController npcG;
	// Name of dungeon
	String name;
	// Contextual array lists for creating dungeons
	ArrayList<Dungeon> rooms = new ArrayList<>();
	ArrayList<Enemy> npc = new ArrayList<>();
	// Random implementation for generating randon integer instances
	Random r = new Random();
	// Current text string used for appending the GUI (parseing)
	public String currentText = "";
	// Player object
	Player player;
	// The selection of the USER
	String option;

	// Constructor generates the town hall of the game
	public Dungeon()

	{
		genTownHall();
	}

	/**
	 * Method intends to return the Dungeons NAME.
	 * @return Dungeon Name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method intends to return the size of the enemy array.
	 * @return Size of enemy array list.
	 */
	public int npcSize() {
		return npc.size();
	}

	/**
	 * Method gets the NPC from the array list.
	 * @return NPC in array list NPC at index "0".
	 */
	public Enemy getNpc() {
		return npc.get(0);
	}

	/**
	 * Method used to check within the game and remove the specified NPC, index [0]
	 */
	public void npcKilled() {
		npc.remove(0);
	}

	/**
	 * Method takes user decision as an option for selecting the dungeon.
	 * @return The dungeon based off of selection (input).
	 */
	public String getOption() {
		return option;
	}

	/**
	 * Sets the options for the genRoom() method.
	 * @param option the parsed in value for option.
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * Initialises the name of the town hall(easy method for doing so in this context).
	 */
	public void genTownHall() {

		name = "Town Hall";

	}

	/**
	 * Generates the "room" or "dungeon" for the game, based off of user input "option"
	 * Theoretically... Names the dungeon, and then instantiates the array list of enemies.
	 * The hard work is performed later when deciding the enemies stats per room in the controller.
	 * 
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void genRoom() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		{
			if (option.equals("1")) {
				name = "The Old Forest";
				npcG = new EnemyController();
				npcG.gen();
				npc = npcG.getNpcs();
			}
			else if (option.equals("2")) {
				name = "The Wandering Isle";
				npcG = new EnemyController();
				npcG.gen();
				npc = npcG.getNpcs();
			}
			else if (option.equals("3")) {
				name = "Black Trail Island";
				npcG = new EnemyController();
				npcG.gen();
				npc = npcG.getNpcs();
			}
			else if (option.equals("4")) {
				name = "Den of Fate";
				npcG = new EnemyController();
				npcG.gen();
				npc = npcG.getNpcs();
			}
			else if (option.equals("5")) {
				name = "Verallions Lair";
				npcG = new EnemyController();
				npcG.genBoss();
				npc = npcG.getNpcs();
			}
			else if (option.equals("0")) {
				name = "The Shop";
			}
		}
	}
}