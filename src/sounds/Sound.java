package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	public Float db = -30.0f;
	public Clip song;

	/**
	 * Constructor loads a file through inputStream
	 * Converts the file into audio stream
	 * and uses AudioSystem to play said song
	 * Float control = volume control for sound. (-30.0f to -15.0f... Preferable)
	 * 
	 * @param songName
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public Sound(String songName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		InputStream path = getClass().getResourceAsStream(songName);
		InputStream bufferedIn = new BufferedInputStream(path);
		
		AudioInputStream audioInputStream = null;		
		audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
		
		song = AudioSystem.getClip();
		song.open(audioInputStream);
		
		// Volume Control
		FloatControl gainControl = (FloatControl) song.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(db);
	}

	/**
	 * Methodplays the currently instantiated sound file.
	 */
	public void play() {
		song.setFramePosition(0);
		song.start();
	}

	/**
	 * Method loops the currently instantiated sound file.
	 */
	public void loop() {
		song.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Method stops the currently instantiated sound file.
	 */
	public void stop() {
		song.stop();
	}
}
