package Controller;

import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Model.Dungeon;
import Model.Fallen;
import Model.Player;
import Model.Story;
import View.Gui;
import sounds.Sound;

public class GameController {

	private Player player;
	private Dungeon room;
	private Gui gui;

	ArrayList<Fallen> fallen = new ArrayList<Fallen>();

	// Constantly being updates to Append TextAreas
	private String message = new String();

	// Error Control Messages
	private String shopError = "";
	private String manaError = "";
	private String combatError = "";
	private String loadError = "";
	private String combatText = "";
	private String nameError = "";

	// Music
	private Sound mainMenu = new Sound("mainMenu.wav");
	private Sound shopTheme = new Sound("shopTheme.wav");
	private Sound healingFountain = new Sound("healingFountain.wav");
	private Sound oldForest = new Sound("theOldForest.wav");
	private Sound wanderingIsle = new Sound("theWanderingIsle.wav");
	private Sound blackTrail = new Sound("blackTrailIsland.wav");
	private Sound denOfFate = new Sound("denOfFate.wav");
	private Sound verallionsLair = new Sound("verallionsLair.wav");

	// Note throws exceptions, necessary from code implementations that
	// potentially throw errors.
	public GameController() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		// Create GUI, Player and NPCgenerator
		createGame();

		// Adding the action listeners to the GUIs buttons
		gui.menuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		gui.menuLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					load();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		gui.menuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		gui.menuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "The Fallen - Text Based Roleplaying Game - Created by Michael Lee",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Initiate game code
		beginStory();
	}

	/**
	 * Method ensures dungeon songs aren't playing. Begins playing main menu song.
	 * Sets the text relative for games beggining. Sets the player stats to
	 * represent default values.
	 *
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	private void beginStory() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

		healingFountain.stop();
		shopTheme.stop();
		oldForest.stop();
		wanderingIsle.stop();
		blackTrail.stop();
		denOfFate.stop();
		verallionsLair.stop();

		// Start MainMenuTheme
		mainMenu.song.start();
		mainMenu.song.loop(Clip.LOOP_CONTINUOUSLY);

		// This setName allows me to loop the loadError system below (to stop
		// game from crashing)
		// player.setName("");

		gui.jta.setText(
				"THE FALLEN - CREATED BY MICHAEL LEE - VERSION 1.0 \n\n- START NEW GAME - \nTo start a new game press (y) then (enter)"
						+ "\n\n- CONTINUE / LOAD SAVE GAME - \nIf you have loaded/or to continue a save game press (r) then (enter) \n\nSHOW FALLEN \nPress (s) to show The Fallen");
		gui.jta.append(loadError);

		checkText();
		if (message.equals("r")) {
			if (player.getName() == null) {
				beginStory();
			} else {
				room = new Dungeon();
				townHall();
			}
		} else if (message.equals("s")) {

			getFallen();
			gui.jta.append("\n\nPress SPACEBAR to continue");
			waitEmpty();

		} else if (message.equals("y")) {
			// Resetting a character
			player.setCurrentLevel(1);
			player.setMaxHitPoints(100);
			player.setHitPoints(100);
			player.setNumPotions(4);
			player.setNumManaPotions(2);
			player.setMinDamage(10);
			player.setMaxDamage(15);
			player.setSpell1Count(5);

			// Sets the spellCounts to 0 for levelling progression
			player.setSpell2Count(0);
			player.setSpell3Count(0);
			player.setSpell4Count(0);
			player.setSpell5Count(0);

			// Select Race Method
			selectRace();

			// Intro Story
			storyIntro();

			// Naming the character Method
			nameCharacter();

			// Generates town hall room and runs method (text)
			room = new Dungeon();
			townHall();
		}
		beginStory();
	}

	/**
	 * Method ensures damage modifiers are stored to control bolster form(spell)
	 * Sets the text throughout the GUI respectively Checks while the array list of
	 * enemies is not empty and the current enemy is alive. Also checks the player
	 * is alive. Sets the enemies stats relative to the players specifically per
	 * dungeon. Sets the image of the enemy to the relevant image. Updates the GUI
	 * with battle text Waits for user input for battle decisions Controls user
	 * input and compares enemy and player variables to determine outputs. Upon room
	 * complete (array list of enemies empty) return to town hall upon SPACEBAR
	 * press.
	 *
	 * GENERIC THROW EXCEPTIONS
	 *
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void startBattle() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// Variables for Spell5 Bolster Form
		int realMinDamage = player.getMinDamage();
		int realMaxDamage = player.getMaxDamage();
		int bolsteredtMinDamage = (int) (player.getMinDamage() * 1.2);
		int bolsteredMaxDamage = (int) (player.getMaxDamage() * 1.2);

		// Resets min/max damage after room complete
		player.setMinDamage(realMinDamage);
		player.setMaxDamage(realMaxDamage);
		int npcHealth = player.getMaxHealth();

		// Starts the right JTA text
		gui.jta2.setText("\nPlayer Information");
		gui.jta3.setText("\nEnemy Information");

		// While the array of NPCs in the room isn't empty
		while (room.npcSize() != 0) {
			if (player.getHitPoints() <= 0) {
				storeFallen();
				endGame();
			}
			player.setCurrentText("");
			// While current NPC is alive
			// Set the current NPCs health and damage accordingly to Room &
			// Player [LOTS OF MATHS]
			if (room.getName().equals("The Old Forest")) {
				// Random floats generates so ENEMY stats change between
				// generation method calls
				float minX = 65.0f;
				float maxX = 95.0f;
				Random rand = new Random();
				float finalX = rand.nextFloat() * (maxX - minX) + minX;

				room.getNpc().setHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMaxHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMinDamage((int) (realMinDamage * (finalX / 100.0f)));
				room.getNpc().setMaxDamage((int) (realMaxDamage * (finalX / 100.0f)));
			} else if (room.getName().equals("The Wandering Isle")) {

				float minX = 85.0f;
				float maxX = 110.0f;
				Random rand = new Random();
				float finalX = rand.nextFloat() * (maxX - minX) + minX;

				room.getNpc().setHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMaxHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMinDamage((int) (realMinDamage * (finalX / 100.0f)));
				room.getNpc().setMaxDamage((int) (realMaxDamage * (finalX / 100.0f)));
			} else if (room.getName().equals("Black Trail Island")) {

				float minX = 105.0f;
				float maxX = 120.0f;
				Random rand = new Random();
				float finalX = rand.nextFloat() * (maxX - minX) + minX;

				room.getNpc().setHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMaxHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMinDamage((int) (realMinDamage * (finalX / 100.0f)));
				room.getNpc().setMaxDamage((int) (realMaxDamage * (finalX / 100.0f)));
			} else if (room.getName().equals("Den of Fate")) {

				float minX = 125.0f;
				float maxX = 140.0f;
				Random rand = new Random();
				float finalX = rand.nextFloat() * (maxX - minX) + minX;

				room.getNpc().setHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMaxHitPoints((int) (npcHealth * (finalX / 100.0f)));
				room.getNpc().setMinDamage((int) (realMinDamage * (finalX / 100.0f)));
				room.getNpc().setMaxDamage((int) (realMaxDamage * (finalX / 100.0f)));
			} else if (room.getName().equals("Verallions Lair")) {
				room.getNpc().setHitPoints((int) (npcHealth * (1000.0f / 100.0f)));
				room.getNpc().setMaxHitPoints((int) (npcHealth * (1000.0f / 100.0f)));
				room.getNpc().setMinDamage((int) (realMinDamage * (250.0f / 100.0f)));
				room.getNpc().setMaxDamage((int) (realMaxDamage * (250.0f / 100.0f)));
			}
			// Picture update for current NPCW
			if (room.getNpc().getName().equals("Warrior")) {
				gui.setNpcPath("resources/npc5.png");
			} else if (room.getNpc().getName().equals("Mage")) {
				gui.setNpcPath("resources/npc1.png");
			} else if (room.getNpc().getName().equals("Priest")) {
				gui.setNpcPath("resources/npc3.png");
			} else if (room.getNpc().getName().equals("Archer")) {
				gui.setNpcPath("resources/npc4.png");
			} else if (room.getNpc().getName().equals("Skeleton")) {
				gui.setNpcPath("resources/npc6.png");
			} else if (room.getNpc().getName().equals("Verallion")) {
				gui.setNpcPath("resources/dragon.png");
			}

			while (room.getNpc().isAlive()) {
				if (player.getHitPoints() <= 0) {
					storeFallen();
					endGame();
				}
				// Sets the images for player/npc to visible for fights.
				appendCombatText();
				appendJta();

				String npcPath = gui.getNpcPath();
				ImageIcon imageNpc = new ImageIcon(getClass().getClassLoader().getResource(npcPath));
				Image image2 = imageNpc.getImage();
				Image newimg2 = image2.getScaledInstance(202, 214, Image.SCALE_SMOOTH);
				ImageIcon imageIcon2 = new ImageIcon(newimg2);

				gui.getNpcLabel().setIcon(imageIcon2);
				gui.playerLabel.setVisible(true);
				gui.npcLabel.setVisible(true);

				// Append the right text box (set & update)
				gui.jta2.setText("- Player Information -");
				gui.jta2.append("\nName: " + player.getName());
				gui.jta2.append("\nRace: " + player.getRace());
				gui.jta2.append("\nLevel: " + player.getCurrentLevel());
				gui.jta2.append("\nExperience: (" + player.getCurrentXp() + "/" + player.getMaxXp() + ")");
				gui.jta2.append("\nMin/Max Damage: (" + player.getMinDamage() + "/" + player.getMaxDamage() + ")");
				gui.jta2.append("\nHealth Potions: " + player.getNumPotions());
				gui.jta2.append("\nMana Potions: " + player.getNumManaPotions());
				gui.jta2.append("\nCoins: " + player.getCash());
				gui.jta2.append("\n\nLocation: " + room.getName());
				gui.jta2.append("\nEnemies Remaining: " + room.npcSize());

				// Append Enemy Text
				gui.jta3.setText("- Enemy Information -");
				gui.jta3.append("\nName: " + room.getNpc().toString());
				gui.jta3.append("\nMin/Max Damage: (" + room.getNpc().getMinDamage() + "/"
						+ room.getNpc().getMaxDamage() + ")");

				gui.jta.append(combatError);
				combatError = "";
				checkText();

				if (message.equals("a")) {
					// Upon enter "a", player and NPC "defend" <- Battle
					room.getNpc().defend(player);
					if (!(room.getNpc().getHitPoints() <= 0)) {
						player.defend(room.getNpc());
					}
					appendCombatText();
					appendJta();

					// Append the right text box (set & update)
					gui.jta2.setText("\nName: " + player.getName());
					gui.jta2.append("\nRace: " + player.getRace());
					gui.jta2.append("\nLevel: " + player.getCurrentLevel());
					gui.jta2.append("\nExperience: (" + player.getCurrentXp() + "/" + player.getMaxXp() + ")");
					gui.jta2.append("\nHealth Potions: " + player.getNumPotions());
					gui.jta2.append("\nMana Potions: " + player.getNumManaPotions());
					gui.jta2.append("\nCoins: " + player.getCash());

					// Append Enemy Text
					gui.jta3.setText("\nName: " + room.getNpc().toString());
					gui.jta3.append("\nMin/Max Damage: (" + room.getNpc().getMinDamage() + "/"
							+ room.getNpc().getMaxDamage() + ")");

				} else if (message.equals("h")) {
					// ANY other key entered, Player uses Health Potion, check
					// if player is dead
					player.heal();
					room.getNpc().setCurrentText("");

					// Append the right text box (set & update)
					gui.jta2.setText("\nName: " + player.getName());
					gui.jta2.append("\nRace: " + player.getRace());
					gui.jta2.append("\nLevel: " + player.getCurrentLevel());
					gui.jta2.append("\nExperience: (" + player.getCurrentXp() + "/" + player.getMaxXp() + ")");
					gui.jta2.append("\nHealth Potions: " + player.getNumPotions());
					gui.jta2.append("\nMana Potions: " + player.getNumManaPotions());
					gui.jta2.append("\nCoins: " + player.getCash());

					// Append Enemy Text
					gui.jta3.setText("\nName: " + room.getNpc().toString());
					gui.jta3.append("\nMin/Max Damage: (" + room.getNpc().getMinDamage() + "/"
							+ room.getNpc().getMaxDamage() + ")");

				} else if (message.equals("s")) {

					// Shows list of castable spells
					spellList();
					gui.jta.append("\n\nPress SPACEBAR then ENTER to continue\n");

					// Checking Text for spell attack
					checkText();

					// Selecting spell
					if (message.equals("1")) {
						if (player.getSpell1Count() >= 1) {
							room.getNpc().defendsp1(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
							}
							player.setSpell1Count(player.getSpell1Count() - 1);
						} else {
							room.getNpc().defend(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
							}
						}
					} else if (message.equals("2")) {
						if (player.getSpell2Count() >= 1) {
							player.setHitPoints(player.getHitPoints() + player.getSpell2());
							gui.jta.append("\nHealing Tide cast (" + player.getSpell2() + " healed");
							player.setSpell2Count(player.getSpell2Count() - 1);
							if (player.getHitPoints() > player.getMaxHitPoints()) {
								player.setHitPoints(player.getMaxHitPoints());
							}
						} else {
							room.getNpc().defend(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
								combatError = "\nYou do not have any remaining charges of Healing Tide, Meleeing\n";
							}
						}

					} else if (message.equals("3")) {
						if (player.getSpell3Count() >= 1) {
							room.getNpc().defendsp3(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
							}
							player.setSpell3Count(player.getSpell3Count() - 1);
						} else {
							room.getNpc().defend(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
							}
							combatError = "\nYou do not have any remaining charges of Shadow Void, Meleeing\n";
						}
					} else if (message.equals("4")) {
						if (player.getSpell4Count() >= 1) {
							realMinDamage = player.getMinDamage();
							realMaxDamage = player.getMaxDamage();
							player.setMinDamage(bolsteredtMinDamage);
							player.setMaxDamage(bolsteredMaxDamage);
							player.setSpell4Count(player.getSpell4Count() - 1);
						} else {
							room.getNpc().defend(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
							}
							combatError = "\nYou do not have any remaining charges of Bolster Form, Meleeing\n";
						}
					} else if (message.equals("5")) {
						if (player.getSpell5Count() >= 1) {
							room.getNpc().defendsp5(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
							}
							player.setSpell5Count(player.getSpell5Count() - 1);
						} else {
							room.getNpc().defend(player);
							if (!(room.getNpc().getHitPoints() <= 0)) {
								player.defend(room.getNpc());
							}
							combatError = "\nYou do not have any remaining charges of Frost Prism, Meleeing\n";
						}
					}

					// Append the right text box (set & update)
					gui.jta2.setText("\nName: " + player.getName());
					gui.jta2.append("\nRace: " + player.getRace());
					gui.jta2.append("\nLevel: " + player.getCurrentLevel());
					gui.jta2.append("\nExperience: (" + player.getCurrentXp() + "/" + player.getMaxXp() + ")");
					gui.jta2.append("\nHealth Potions: " + player.getNumPotions());
					gui.jta2.append("\nMana Potions: " + player.getNumManaPotions());
					gui.jta2.append("\nCoins: " + player.getCash());

					// Append Enemy Text
					gui.jta3.setText("\nName: " + room.getNpc().toString());
					gui.jta3.append("\nMin/Max Damage: (" + room.getNpc().getMinDamage() + "/"
							+ room.getNpc().getMaxDamage() + ")");

					// Set left box text to Encounter text
					gui.jta.setText("\nYou encounter " + room.getNpc().toString() + " (" + room.getNpc().getHealth()
							+ "/" + room.getNpc().getMaxHealth() + ")\n");

				} else if (message.equals("m")) {
					manaPotion();
				} else if (message.equals("r")) {
					gui.jta.append(
							"\nWarning leaving a dungeon in progress will reset the instance! \n\nYou will also lose 100 experience points and 50 coins \n\nPress (y) to confirm leaving\n");
					checkText();
					if (message.equals("y")) {
						if (player.getCurrentXp() >= 100) {
							player.setCurrentXp(player.getCurrentXp() - 100);
						}
						if (player.getCash() >= 50) {
							player.setCash(player.getCash() - 50);
						}
						townHall();
					}
				}
			}
			// Potion gain system
			int tempPotionCount = player.getNumPotions();
			int tempManaPotionCount = player.getNumManaPotions();
			// Calls method that increases player stats post battle
			experienceGain();
			int postPotionCount = player.getNumPotions();
			int postManaPotionCount = player.getNumManaPotions();

			// Appends JTA with new stats and room clear
			// Test (fix this so happens at end of npc and displays correct
			// INFORMATION
			gui.jta.append("\n100 Experience Points");
			gui.jta.append("\n" + (postPotionCount - tempPotionCount) + " health potions" + " and "
					+ (postManaPotionCount - tempManaPotionCount) + " mana potions");
			gui.jta.append("\n\nPress SPACEBAR to continue your journey through " + room.getName() + "\n");

			if (room.npcSize() == 0) {
				gui.jta.append("\n" + room.getName() + " has been cleared \nYou gain an addtional 100 bonus coins");
				player.setCash(player.getCash() + 100);
			}

			// Waits for input to continue
			waitEmpty();
		}
		// Room complete return to townHall
		townHall();
	}

	/**
	 * Method instantiates a new GUI from view, and a player from model.
	 *
	 * @throws IOException
	 */
	private void createGame() throws IOException {
		gui = new Gui();
		player = new Player();
	}

	/**
	 * Method appends the GUI with relevant text that asks user to select a RACE.
	 * Sets the variables of the player based on floating point scaling (Race math
	 * calculations).
	 */
	private void selectRace() {
		gui.jta.setText("Welcome to the world of Endal" + "\n" + "\nSelect your race:\n"
				+ "\n1. Human [10% More Health & 10% More Damage]\n" + "\n2. Elf [05% More Health & 15% More Damage]\n"
				+ "\n3. Orc [05% More Health & 15% More Damage]\n"
				+ "\n4. Dwarf [10% More Health & 10% More Damage]\n ");

		checkText();
		// Using floats I am able to increase and type cast player attributes
		// based on Race selected by a percentage
		if (message.equals("1")) {
			player.setRace("Human");
			player.setHitPoints((int) (player.getHitPoints() * (110.0f / 100.0f)));
			player.setMaxHitPoints((int) (player.getMaxHitPoints() * (110.0f / 100.0f)));
			player.setMinDamage((int) (player.getMinDamage() * (110.0f / 100.0f)));
			player.setMaxDamage((int) (player.getMaxDamage() * (110.0f / 100.0f)));
		} else if (message.equals("2")) {
			player.setRace("Elf");
			player.setHitPoints((int) (player.getHitPoints() * (105.0f / 100.0f)));
			player.setMaxHitPoints((int) (player.getMaxHitPoints() * (105.0f / 100.0f)));
			player.setMinDamage((int) (player.getMinDamage() * (115.0f / 100.0f)));
			player.setMaxDamage((int) (player.getMaxDamage() * (115.0f / 100.0f)));
		} else if (message.equals("3")) {
			player.setRace("Orc");
			player.setHitPoints((int) (player.getHitPoints() * (105.0f / 100.0f)));
			player.setMaxHitPoints((int) (player.getMaxHitPoints() * (105.0f / 100.0f)));
			player.setMinDamage((int) (player.getMinDamage() * (115.0f / 100.0f)));
			player.setMaxDamage((int) (player.getMaxDamage() * (115.0f / 100.0f)));
		} else if (message.equals("4")) {
			player.setRace("Dwarf");
			player.setHitPoints((int) (player.getHitPoints() * (110.0f / 100.0f)));
			player.setMaxHitPoints((int) (player.getMaxHitPoints() * (110.0f / 100.0f)));
			player.setMinDamage((int) (player.getMinDamage() * (110.0f / 100.0f)));
			player.setMaxDamage((int) (player.getMaxDamage() * (110.0f / 100.0f)));
		} else {
			player.setRace("Human");
			player.setHitPoints((int) (player.getHitPoints() * (110.0f / 100.0f)));
			player.setMaxHitPoints((int) (player.getMaxHitPoints() * (110.0f / 100.0f)));
			player.setMinDamage((int) (player.getMinDamage() * (110.0f / 100.0f)));
			player.setMaxDamage((int) (player.getMaxDamage() * (110.0f / 100.0f)));
		}
	}

	/**
	 * Method sets text on GUI, Sanitises user input to remove spaces, prints error
	 * if wrong input, else stores string into player mode name
	 */
	private void nameCharacter() {
		gui.jta.setText("\nPlease Enter your name to continue...\n");
		gui.jta.append(nameError);
		checkTextName();
		String st = message.replaceAll("\\s+", "");
		if (st.equals("")) {
			nameError = "\nYou entered an invalid name. Try again.";
			nameCharacter();
		} else {
			nameError = "";
			player.setName(st);
		}
	}

	/**
	 * Method begins the town hall, stopping the unnecessary music files. Appends
	 * the GUI to relevant information or town hall. Waits for text to be entered
	 * from user to navigate the game.
	 *
	 *
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	private void townHall() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// Stops any playing song and plays mainMenu theme
		mainMenu.song.start();
		mainMenu.song.loop(Clip.LOOP_CONTINUOUSLY);
		healingFountain.stop();
		shopTheme.stop();
		oldForest.stop();
		wanderingIsle.stop();
		blackTrail.stop();
		denOfFate.stop();
		verallionsLair.stop();

		// Sets the images for player/npc to visible for fights.
		gui.playerLabel.setVisible(true);
		gui.npcLabel.setVisible(false);
		// Append the right text box (set & update) to correct leaving a room
		gui.jta2.setText("- Player Information -");
		gui.jta2.append("\nName: " + player.getName());
		gui.jta2.append("\nRace: " + player.getRace());
		gui.jta2.append("\nLevel: " + player.getCurrentLevel());
		gui.jta2.append("\nExperience: (" + player.getCurrentXp() + "/" + player.getMaxXp() + ")");
		gui.jta2.append("\nMin/Max Damage: (" + player.getMinDamage() + "/" + player.getMaxDamage() + ")");
		gui.jta2.append("\nHealth Potions: " + player.getNumPotions());
		gui.jta2.append("\nMana Potions: " + player.getNumManaPotions());
		gui.jta2.append("\nCoins: " + player.getCash());
		gui.jta2.append("\n\nLocation: Elderhaven");
		gui.jta3.setText("");

		gui.jta.setText("Welcome " + player.getName() + " to the world of Endal. \n"
				+ "You're in the town of Elderhaven, a safe zone. \n"
				+ "\nPress (g) for Game-Guide\nPress (s) then (enter) for a list of spells\n"
				+ "\nWhere would you like to travel to :\n"
				+ "\n[Menu] \nm. Main Menu \n\n[Safe-Zones] \n0. The Shop \n9. Healing Fountain"
				+ "\n\n[Not Safe-Zones] \n1. The Old Forest [Levels: 1-3]" + "\n2. The Wandering Isle [Levels: 4-6]"
				+ "\n3. Black Trail Island [Levels: 7-9]" + "\n4. Den of Fate [Levels: 10-12]"
				+ "\n5. Verallions Lair [Levels: 13-15]");

		checkText();

		if (message.equals("9")) {
			mainMenu.stop();
			healingFountain.song.start();
			healingFountain.song.loop(Clip.LOOP_CONTINUOUSLY);
			gui.jta.setText("\nYou are about to enter The Healing Fountain" + "\nDo you wish to continue (y) or (n) ?");
			checkText();
			if (message.equals("y")) {
				room.setOption("9");
				startHealingFountain();
			} else
				townHall();
		} else if (message.equals("0")) {
			mainMenu.stop();
			shopTheme.song.start();
			shopTheme.song.loop(Clip.LOOP_CONTINUOUSLY);
			gui.jta.setText("\nYou are about to enter The Shop" + "\nDo you wish to continue (y) or (n) ?");

			checkText();
			if (message.equals("y")) {
				room.setOption("0");
				startShop();
			} else
				townHall();
		} else if (message.equals("1")) {
			mainMenu.stop();
			oldForest.song.start();
			oldForest.song.loop(Clip.LOOP_CONTINUOUSLY);
			gui.jta.setText(
					"\nYou are about to enter The Old Forest [Very Easy]" + "\nDo you wish to continue (y) or (n) ?");

			checkText();
			if (message.equals("y")) {
				room.setOption("1");
				// Generates room based on current "option" variable
				room.genRoom();
				storyMessage();
				startBattle();
			} else
				townHall();
		} else if (message.equals("2")) {
			mainMenu.stop();
			wanderingIsle.song.start();
			wanderingIsle.song.loop(Clip.LOOP_CONTINUOUSLY);
			gui.jta.setText(
					"\nYou are about to enter The Wandering Isle [Easy]" + "\nDo you wish to continue (y) or (n) ?");

			checkText();
			if (message.equals("y")) {
				room.setOption("2");
				// Generates room based on current "option" variable
				room.genRoom();
				storyMessage();
				startBattle();
			} else
				townHall();
		} else if (message.equals("3")) {
			mainMenu.stop();
			blackTrail.song.start();
			blackTrail.song.loop(Clip.LOOP_CONTINUOUSLY);
			gui.jta.setText(
					"\nYou are about to enter Black Trail Island [Medium]" + "\nDo you wish to continue (y) or (n) ?");

			checkText();
			if (message.equals("y")) {
				room.setOption("3");
				// Generates room based on current "option" variable
				room.genRoom();
				storyMessage();
				startBattle();
			} else
				townHall();
		} else if (message.equals("4")) {
			mainMenu.stop();
			denOfFate.song.start();
			denOfFate.song.loop(Clip.LOOP_CONTINUOUSLY);
			gui.jta.setText("\nYou are about to enter Den of Fate [Hard]" + "\nDo you wish to continue (y) or (n) ?");

			checkText();
			if (message.equals("y")) {
				room.setOption("4");
				// Generates room based on current "option" variable
				room.genRoom();
				storyMessage();
				startBattle();
			} else
				townHall();
		} else if (message.equals("5")) {
			mainMenu.stop();
			verallionsLair.song.start();
			verallionsLair.song.loop(Clip.LOOP_CONTINUOUSLY);
			gui.jta.setText(
					"\nYou are about to enter Verallions Lair [Very Hard]" + "\nDo you wish to continue (y) or (n) ?");

			checkText();
			if (message.equals("y")) {
				room.setOption("5");
				// Generates room based on current "option" variable
				room.genRoom();
				storyMessage();
				startBattle();
			} else
				townHall();
		} else if (message.equals("s")) {
			spellList();
			gui.jta.append("\n\nPress SPACEBAR to continue\n");
			waitEmpty();
			townHall();
		} else if (message.equals("g")) {

			gui.jta.setText("- Game Guide - "
					+ "\n\nHello and welcome to The Fallen. \nI hope you enjoy playing it and provide valuable feedback to help improve the games quality"
					+ "\n\nDevelopper: \nMichael Lee \n\nTesters: \nMichael Lee \nJack Allen \nNeil Millward\nThomas Barnes "
					+ "\n\nGraphics: \nJack Allen \n\nSound: \nJay Man (youtube.com/c/ourmusicbox)");

			gui.jta.append("\n\nPress SPACEBAR to continue\n");
			waitEmpty();
			townHall();
		} else if (message.equals("m")) {
			// Takes you to main menu
			beginStory();
		} else {
			townHall();
		}
	}

	private void spellList() {
		if (player.getCurrentLevel() == 1) {
			gui.jta.append("\n\nSpell List:\n" + "1. Flame Strike: " + player.getSpell1() + " damage ("
					+ player.getSpell1Count() + " charges remaining)\n" + "2. Healing Tide(Unlocked at level 2)\n"
					+ "3. Shadow Void(Unlocked at level 3)\n" + "4. Bolster Form(Unlocked at level 4)\n"
					+ "5. Frost Prism(Unlocked at level 5)\n");
		} else if (player.getCurrentLevel() == 2) {
			gui.jta.append("\n\nSpell List:\n" + "1. Flame Strike: " + player.getSpell1() + " damage ("
					+ player.getSpell1Count() + " charges remaining)\n" + "2. Healing Tide: " + player.getSpell2() + "("
					+ player.getSpell2Count() + " charges remaining)\n" + "3. Shadow Void(Unlocked at level 3)\n"
					+ "4. Bolster Form(Unlocked at level 4)\n" + "5. Frost Prism(Unlocked at level 5)\n");
		} else if (player.getCurrentLevel() == 3) {
			gui.jta.append("\n\nSpell List:\n" + "1. Flame Strike: " + player.getSpell1() + " damage ("
					+ player.getSpell1Count() + " charges remaining)\n" + "2. Healing Tide: " + player.getSpell2() + "("
					+ player.getSpell2Count() + " charges remaining)\n" + "3. Shadow Void: " + player.getSpell3() + "("
					+ player.getSpell3Count() + " charges remaining)\n" + "4. Bolster Form(Unlocked at level 4)\n"
					+ "5. Frost Prism(Unlocked at level 5)\n");
		} else if (player.getCurrentLevel() == 4) {
			gui.jta.append("\n\nSpell List:\n" + "1. Flame Strike: " + player.getSpell1() + " damage ("
					+ player.getSpell1Count() + " charges remaining)\n" + "2. Healing Tide: " + player.getSpell2() + "("
					+ player.getSpell2Count() + " charges remaining)\n" + "3. Shadow Void: " + player.getSpell3() + "("
					+ player.getSpell3Count() + " charges remaining)\n" + "4. Bolster Form: " + player.getSpell4() + "("
					+ player.getSpell4Count() + " charges remaining)\n" + "5. Frost Prism(Unlocked at level 5)\n");
		} else if (player.getCurrentLevel() == 5) {
			gui.jta.append("\n\nSpell List:\n" + "1. Flame Strike: " + player.getSpell1() + " damage ("
					+ player.getSpell1Count() + " charges remaining)\n" + "2. Healing Tide: " + player.getSpell2() + "("
					+ player.getSpell2Count() + " charges remaining)\n" + "3. Shadow Void: " + player.getSpell3() + "("
					+ player.getSpell3Count() + " charges remaining)\n" + "4. Bolster Form: " + player.getSpell4() + "("
					+ player.getSpell4Count() + " charges remaining)\n" + "5. Healing Tide: " + player.getSpell5() + "("
					+ player.getSpell5Count() + " charges remaining)\n");
		}
	}

	/**
	 * Method appends gui with a list of available options
	 */
	private void commandList() {
		gui.jta.append(
				"\n[a] to attack \n[s] for spell list \n[h] consume healing potion [20% heal] \n[m] consume mana potion [+2 charges] \n[r] to run\n");
	}

	/**
	 * Method used to calculate the end of a fight between player and enemy Updates
	 * player experience and generates random amounts of coins for player. Adds
	 * between 1 and 2 health and mana potions to players inventory. Checks what
	 * level the player is to stop levelling in low level areas. If the player has
	 * levelled, update the model stats accordingly.
	 */
	private void experienceGain() {
		// Remove [0] From ArrayList of NPCs
		room.npcKilled();

		// Generates random coins
		Random randomCash = new Random();
		int randomCashInt = randomCash.nextInt(50 - 1) + 1;
		player.setCash(player.getCash() + randomCashInt);

		// Adds 1-2 hp pots per fight
		Random randomPotion = new Random();
		int randomPotionInt = randomPotion.nextInt(2 - 1) + 1;
		player.setNumPotions(player.getNumPotions() + randomPotionInt);

		// Adds 0-2 mana pots per fight
		Random randomManaPotion = new Random();
		int randomManaPotionInt = randomManaPotion.nextInt(1 - 0) + 1;
		player.setNumManaPotions(player.getNumManaPotions() + randomManaPotionInt);

		if (room.getName().equals("The Old Forest")) {
			if (player.getCurrentLevel() < 3) {
				player.setCurrentXp(player.getCurrentXp() + 100);
			}
		} else if (room.getName().equals("The Wandering Isle")) {
			if (player.getCurrentLevel() >= 3 && player.getCurrentLevel() < 6) {
				player.setCurrentXp(player.getCurrentXp() + 100);
			}
		} else if (room.getName().equals("Black Trail Island")) {
			if (player.getCurrentLevel() >= 6 && player.getCurrentLevel() < 9) {
				player.setCurrentXp(player.getCurrentXp() + 100);
			}
		} else if (room.getName().equals("Den of Fate")) {
			if (player.getCurrentLevel() >= 9 && player.getCurrentLevel() < 12) {
				player.setCurrentXp(player.getCurrentXp() + 100);
			}
		} else if (room.getName().equals("Verallions Lair")) {
			if (player.getCurrentLevel() >= 12 && player.getCurrentLevel() < 15) {
				player.setCurrentXp(player.getCurrentXp() + 100);
			}
		}

		if (player.getCurrentXp() >= player.getMaxXp()) {
			player.setPreviousLevel(player.getCurrentLevel());
			player.setCurrentLevel(player.getCurrentLevel() + 1);

			// Increments the XP total (maxXp) so that levelling has progression
			player.incrementExperience();

			// Hard reset on Player XP count
			player.setCurrentXp(0);

			// Updates Player stats
			player.setHitPoints((int) (player.getHitPoints() * (120.0f / 100.0f)));
			player.setMaxHitPoints((int) (player.getMaxHitPoints() * (120.0f / 100.0f)));
			player.setMinDamage((int) (player.getMinDamage() * (120.0f / 100.0f)));
			player.setMaxDamage((int) (player.getMaxDamage() * (120.0f / 100.0f)));

			player.setSpell1((int) (player.getSpell1() * (120.0f / 100.0f)));
			player.setSpell2((int) (player.getSpell2() * (120.0f / 100.0f)));
			player.setSpell3((int) (player.getSpell3() * (120.0f / 100.0f)));
			player.setSpell4((int) (player.getSpell4() * (120.0f / 100.0f)));
			player.setSpell5((int) (player.getSpell5() * (120.0f / 100.0f)));

			gui.jta.append("\nYou have become level " + player.getCurrentLevel()
					+ "\nYou gained 20% more health and 20% more damage" + "\nYou learnt a new spell"
					+ "\nVisit a nearby safe-zone or use a mana potion to increase spell charges");

			gui.jta.append("\n\n- LOOT - \n\nPlayer gained: " + randomCashInt + " Coins");
		}
	}

	public void startShop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		gui.jta.setText("Welcome to the shop " + player.getName());
		gui.jta.append("\nHere is a list of items for sale: " + "\n1. Health Potion [50 Coin]"
				+ "\n2. Mana Potion [50 Coin] \n3. Exit Shop");
		gui.jta.append("\nYou have: " + player.getCash() + " Coins");
		gui.jta.append("\n\n" + shopError);

		shopError = "";
		checkText();
		if (message.equals("1")) {
			if (player.getCash() < 50) {
				shopError = "You have insufficient funds to purchase: Health Potion [50 Coin]";
				startShop();
			} else {
				player.setCash(player.getCash() - 50);
				player.setNumPotions(player.getNumPotions() + 1);
				// Append the right text box (set & update)
				gui.jta2.setText("- Player Information -");
				gui.jta2.append("\nName: " + player.getName());
				gui.jta2.append("\nRace: " + player.getRace());
				gui.jta2.append("\nLevel: " + player.getCurrentLevel());
				gui.jta2.append("\nExperience: (" + player.getCurrentXp() + "/" + player.getMaxXp() + ")");
				gui.jta2.append("\nMin/Max Damage: (" + player.getMinDamage() + "/" + player.getMaxDamage() + ")");
				gui.jta2.append("\nHealth Potions: " + player.getNumPotions());
				gui.jta2.append("\nMana Potions: " + player.getNumManaPotions());
				startShop();
			}

		} else if (message.equals("2")) {
			if (player.getCash() < 50) {
				shopError = "You have insufficient funds to purchase: Health Potion [50 Coin]";
				startShop();
			} else {
				player.setCash(player.getCash() - 50);
				player.setNumManaPotions(player.getNumManaPotions() + 1);
				// Append the right text box (set & update)
				gui.jta2.setText("- Player Information -");
				gui.jta2.append("\nName: " + player.getName());
				gui.jta2.append("\nRace: " + player.getRace());
				gui.jta2.append("\nLevel: " + player.getCurrentLevel());
				gui.jta2.append("\nExperience: (" + player.getCurrentXp() + "/" + player.getMaxXp() + ")");
				gui.jta2.append("\nMin/Max Damage: (" + player.getMinDamage() + "/" + player.getMaxDamage() + ")");
				gui.jta2.append("\nHealth Potions: " + player.getNumPotions());
				gui.jta2.append("\nMana Potions: " + player.getNumManaPotions());
				startShop();
			}
		}
		townHall();
	}

	/**
	 * Begins the healing fountain (room), that allows the player to heal on
	 * command.
	 *
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void startHealingFountain() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		gui.jta.setText("Welcome to the Healing Fountain " + player.getName());
		gui.jta.append("\n\nPress 1 to heal yourself \nPress 2 to leave");
		gui.jta.append("\n\nCurrent Player Health: " + player.getHitPoints() + "/" + player.getMaxHitPoints());

		checkText();
		if (message.equals("1")) {
			player.setHitPoints(player.getMaxHitPoints());
			startHealingFountain();
		} else {
			townHall();
		}
	}

	/**
	 * Method is designed to short-hand the battle text.
	 */
	public void appendJta() {
		gui.jta.setText("Currently in battle with: " + room.getNpc().getName() + "\n\n");
		gui.jta.append(player.getName() + " (" + player.getHealth() + "/" + player.getMaxHealth() + ")");
		gui.jta.append("\n" + room.getNpc().getName() + " (" + room.getNpc().getHealth() + "/"
				+ room.getNpc().getMaxHealth() + ")\n");
		gui.jta.append(combatText);
		commandList();
	}

	/**
	 * Method is designed to short-hand the battle text.
	 */
	public void appendCombatText() {
		combatText = player.getCurrentText() + room.getNpc().getCurrentText() + "\n";
	}

	/**
	 * Instantiate a new story intro from the model. Allows for story messages to be
	 * passed into the GUI on command.
	 */
	public void storyIntro() {
		Story story = new Story();
		story.genIntro(player.getRace());
		gui.jta.setText(story.getStory());
		gui.jta.append("\nPress SPACEBAR to continue");
		waitEmpty();
		emptyText();
	}

	/**
	 * Instantiate a new story message from the model. Allows for story messages to
	 * be passed into the gui on command.
	 */
	public void storyMessage() {
		Story story = new Story();
		story.genStoryMessage(room.getName());
		gui.jta.setText(story.getStory());
		gui.jta.append("\nPress SPACEBAR to continue");
		waitEmpty();
		emptyText();
	}

	/**
	 * Implements the waitEnter() method that waits for the user to press Enter
	 * before proceeding. Also sets the current games message variable to the
	 * JTextAreas text Empties the text post-enter key.
	 */
	public void checkTextName() {
		waitEnter();
		message = gui.jtf.getText();
		emptyText();
	}

	/**
	 * Implements the waitEnter() method that waits for the user to press Enter
	 * before proceeding. Also sets the current games message variable to the
	 * JTextAreas text Empties the text post-enter key.
	 */
	public void checkText() {
		while (gui.jtf.getText().equals("")) {
			waitEnter();
		}
		message = gui.jtf.getText();
		emptyText();
	}

	/**
	 * Empties the text within the JTextArea.
	 */
	public void emptyText() {
		if (!gui.jtf.getText().equals("")) {
			gui.jtf.setText("");
		}
	}

	/**
	 * Method checks if the player has no mana potions and throws error. Else the
	 * method allocates 2 more charges to each spell.
	 */
	private void manaPotion() {
		if (player.getNumManaPotions() <= 0) {
			manaError = "You do not have any Mana Potions remaining";
			gui.jta.append("\n" + manaError);
		} else {
			player.setNumManaPotions(player.getNumManaPotions() - 1);
			player.setSpell1Count(player.getSpell1Count() + 2);
			player.setSpell2Count(player.getSpell2Count() + 2);
			player.setSpell3Count(player.getSpell3Count() + 2);
			player.setSpell4Count(player.getSpell4Count() + 2);
			player.setSpell5Count(player.getSpell5Count() + 2);
		}
	}

	// Ends the game and resets
	/**
	 * Sets the gui text accordingly. Ends the game resetting the player and
	 * beggining a new story.
	 *
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	public void endGame() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		gui.jta2.setText("");
		gui.jta3.setText("");
		gui.playerLabel.setVisible(false);
		gui.npcLabel.setVisible(false);
		player.resetPlayer();
		beginStory();
	}

	// Stores when a player falls in battle
	/**
	 * Method stores the fallen characters in the model for fallen characters.
	 */
	public void storeFallen() {
		Fallen fallenPlayer = new Fallen();
		fallenPlayer.setName(player.getName());
		fallenPlayer.setLevel(player.getCurrentLevel());
		fallenPlayer.setRace(player.getRace());
		fallenPlayer.setWhereFell(room.getName());
		fallen.add(fallenPlayer);
	}

	/**
	 * Method is used to append the GUI with the list of fallen characters using a
	 * for reach loop.
	 */
	public void getFallen() {
		gui.jta.append("\n\n- THE FALLEN -");
		for (Fallen text : fallen) {
			gui.jta.append("\nCharacter Name: " + text.getName());
			gui.jta.append(", Character Race: " + text.getRace());
			gui.jta.append(", Character Level: " + text.parseString());
			gui.jta.append(", Dungeon Fell: " + (text.getWhereFell()));
		}
	}

	/**
	 * Method waits on SPACEBAR key press to proceed with the code.
	 */
	public void waitEmpty() {

		final CountDownLatch latch = new CountDownLatch(1);

		KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					latch.countDown();
				return false;
			}
		};

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
	}

	/**
	 * Method waits for keyboard press ENTER, only active if the textField is not
	 * empty.
	 */
	public void waitEnter() {

		final CountDownLatch latch = new CountDownLatch(1);

		KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !gui.jtf.getText().equals(""))
					latch.countDown();
				return false;
			}
		};

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
	}

	/**
	 * Save method that generates a .DAT file and saves the relevant data from the
	 * model. Uses input and output streams to do so. JFileChooser (selection window
	 * for gui)
	 */
	public void save() {
		JFileChooser dialog = new JFileChooser();

		int returnVal = dialog.showOpenDialog(gui);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = dialog.getSelectedFile().getAbsolutePath();
			if (!filename.endsWith(".dat"))
				filename += ".dat";

			FileOutputStream s;
			try {
				s = new FileOutputStream(filename);
				ObjectOutputStream os = new ObjectOutputStream(s);

				os.writeObject(player.getName());
				os.writeObject(player.getRace());
				os.writeObject(player.getMaxHitPoints());
				os.writeObject(player.getHitPoints());
				os.writeObject(player.getNumPotions());
				os.writeObject(player.getNumManaPotions());
				os.writeObject(player.getMinDamage());
				os.writeObject(player.getMaxDamage());
				os.writeObject(player.getSpell1());
				os.writeObject(player.getSpell2());
				os.writeObject(player.getSpell3());
				os.writeObject(player.getSpell4());
				os.writeObject(player.getSpell5());
				os.writeObject(player.getSpell1Count());
				os.writeObject(player.getSpell2Count());
				os.writeObject(player.getSpell3Count());
				os.writeObject(player.getSpell4Count());
				os.writeObject(player.getSpell5Count());

				os.writeObject(player.getCurrentXp());
				os.writeObject(player.getMaxXp());
				os.writeObject(player.getCurrentLevel());
				os.writeObject(player.getPreviousLevel());
				os.writeObject(player.getMaxLevel());
				os.writeObject(player.getCash());

				for (int k = 0; k != fallen.size(); k++) {
					String currentName = fallen.get(k).getName();
					String currentRace = fallen.get(k).getRace();
					int currentLevel = fallen.get(k).getLevel();
					String currentWhereFell = fallen.get(k).getWhereFell();
					os.writeObject(currentName);
					os.writeObject(currentRace);
					os.writeObject(currentLevel);
					os.writeObject(currentWhereFell);
				}

				os.writeObject(null);
				os.close();
				s.close();
				os.flush();
				s.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// Save, Load Methods
	/**
	 * Load method for the game, loads from .DAT file specified. Reads the objects
	 * from the file into the model, theoretically continuing the game.
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void load() throws IOException, ClassNotFoundException {
		fallen.clear();
		JFileChooser dialog = new JFileChooser();

		if (dialog.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
			String filename = dialog.getSelectedFile().getAbsolutePath();
			if (!filename.endsWith(".dat"))
				filename += ".dat";

			FileInputStream s = null;
			try {
				s = new FileInputStream(filename);
				ObjectInputStream os = new ObjectInputStream(s);

				player.setName((String) os.readObject());
				player.setRace((String) os.readObject());
				player.setMaxHitPoints((int) os.readObject());
				player.setHitPoints((int) os.readObject());
				player.setNumPotions((int) os.readObject());
				player.setNumManaPotions((int) os.readObject());
				player.setMinDamage((int) os.readObject());
				player.setMaxDamage((int) os.readObject());
				player.setSpell1((int) os.readObject());
				player.setSpell2((int) os.readObject());
				player.setSpell3((int) os.readObject());
				player.setSpell4((int) os.readObject());
				player.setSpell5((int) os.readObject());
				player.setSpell1Count((int) os.readObject());
				player.setSpell1Count((int) os.readObject());
				player.setSpell1Count((int) os.readObject());
				player.setSpell1Count((int) os.readObject());
				player.setSpell1Count((int) os.readObject());

				player.setCurrentXp((int) os.readObject());
				player.setMaxXp((int) os.readObject());
				player.setCurrentLevel((int) os.readObject());
				player.setPreviousLevel((int) os.readObject());
				player.setMaxLevel((int) os.readObject());
				player.setCash((int) os.readObject());

				try {
					while (os != null) {
						Fallen currentFallen = new Fallen();
						currentFallen.setName((String) os.readObject());
						currentFallen.setRace((String) os.readObject());
						currentFallen.setLevel((int) os.readObject());
						currentFallen.setWhereFell((String) os.readObject());
						fallen.add(currentFallen);
					}
				} catch (EOFException exc) {
					os.close();
					s.close();
				}
			} finally {
				s.close();
			}
		}
	}
}