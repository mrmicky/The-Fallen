package GameLoader;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Controller.GameController;

public class GameLoader {

	/**
	 * This class loads the game by instantiating a new GameController.
	 * 
	 */
	public static void main(String[] args) {
		try {
			GameController a = new GameController();
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
