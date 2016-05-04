package Model;

public class Story {

	String story;

	public Story() {
		this.story = story;
	}

	/**
	 * 
	 * @return Returns the string of story.
	 */
	public String getStory() {
		return story;
	}

	/**
	 * Sets the string for story.
	 * @param story
	 */
	public void setStory(String story) {
		this.story = story;
	}

	// Going to take in a int based on playerRace
	/**
	 * Generates an intro story sequence for the game.
	 * @param s
	 */
	public void genIntro(String s) {
		if (s.equals("Human")) {
			setStory(
					"\nFifteen years after Thaurissian laid ruin to Endals greatest cities his son Verallion threatens to do much more. \n\nYour father King Lane led the last Human army against Thaurissian, stories told speak of how your fathers final action before subsiding to his injures was piercing the mighty Verallions heart with his blade “Omen”. \n\nThe last Humans reside in the city of Elderhaven, a peaceful place; untouched by the evil that belongs to the environments surrounding. Guarded by the Imperial Elite, and Mage of the Dawn.\n\nYou, like your father have a touch of destiny. Verallion threatens to destroy everything we have, and you must find a way to stop him, once and for all… \n\nYour travels bring you to a town nearby Elderhaven.\n");
		} else if (s.equals("Elf")) {
			setStory("\nFifteen years after Thaurissian laid ruin to Endals greatest cities his son Verallion threatens to do much more. \n\nWord has reached you Elf that your brother Aurilius has been spotted leaving our kingdom Oakwood this morning. \n\nWe know his plan, to pre-emptively attack Verallion before the mighty dragon is able to muster a force that could wipe the Elves off the face of Endal. \n\nWe also know he is naïve to think that Verallion will not see this coming… \n\nYou must leave Oakwood and find your brother, and put end to his foolish attempts to fight Verallion alone...\n\nYour travels bring you to a town nearby Elderhaven.\n");
		} else if (s.equals("Orc")) {
			setStory("\nFifteen years after Thaurissian laid ruin to Endals greatest cities his son Verallion threatens to do much more. \n\nWar Chief Gromgar, your father; bear witness to the king of humans King Lane deliver the final blow to Verallion before perishing. \n\nHe led the army of Orc and Man combined and beat back the forces of Thaurissian. \n\nHe was killed before he could return home by Verallion. \n\nYou’re his son, his legacy, the way of the Orc is to fight and die with honour. \n\nFor your father you leave your home of Dunglehold with one thought, revenge.\n\nYour travels bring you to a town nearby Elderhaven.\n");
		} else if (s.equals("Dwarf")) {
			setStory("\nFifteen years after Thaurissian laid ruin to Endals greatest cities his son Verallion threatens to do much more. \n\nBardul, your father; led the Dwarves into battle against the forces of Thaurissian along-side the leaders of Man and Orc. \n\nThe weapons they rose against the mighty dragon were forged in the fires of your home town Steelforgemore. \n\nThe weapon that brought down Thaurissian; “Omen” was bestowed upon the King of man by Bardul. \n\nIn his old age Bardul commands you Prince of Dwarves to honour his legacy and travel to Verallions lair, seek out the beast and destroy it, bringing peace to the people once more.\n\nYour travels bring you to a town nearby Elderhaven.\n");
		}
	}

	/** 
	 * Generates a story message for the game.
	 * @param s
	 */
	public void genStoryMessage(String s) {
		if (s.equals("The Old Forest")) {
			setStory("\nAs you enter The Old Forest you notice enemies in the distance. \n\nThe townspeople spoke of bandits gathering in the forest waiting for innocents to pick-pocket. \n\nUnfortunately for them, you have no time or patience for criminals… \n\nYou charge into battle\n");
		} else if (s.equals("The Wandering Isle")) {
			setStory("\nYou have entered The Wandering Isle \n\nYou can see glowing eyes all around you, the stench of death fills the air \n\nAs you step forward you hear something to your left, you turn to face the noises origin \n\nEnemies stand in your way, you leap into battle\n");
		} else if (s.equals("Black Trail Island")) {
			setStory("\nYou find yourself on Black Trail Island, known for its unfriendly inhabitants and pirating bandits \n\nBefore you can take your first step the sound of canon fire can be heard \n\nYou ready your weapon and head towards the noise \n\nEnemies surround you, before you can think what to do you’re engaged in combat.");
		} else if (s.equals("Den of Fate")) {
			setStory("\nYou crawl inside the Den of Fate, despite the entrance being narrow from the inside the Den is humungous \n\nNoises echo all around you, you hear scraping coming from the darkness that fills the room \n\nYou light a torch, it illuminates the room… \n\nYou almost wish it had not, the room is filled with over worldly monsters that immediately attack.");
		} else if (s.equals("Verallions Lair")) {
			setStory("\nYou enter Verallions Lair. Upon entering you stand in awe at the sheer size of the beast before you \n\nVerallion stands in wait, over twenty feet tall \n\nThe Dragon exposes his weakness as he shows off his impressive size, his lighter armoured chest \n\nPiercing that will grant access to the dragons heart \n\nYou be able to end this fight once and for all \n\nDespite the feeling of hopelessness in the company of such a powerful beast \n\nYou clutch the hilt of your sword and charge into the fire \n\nWhere no man had gone before");
		}
	}
}
