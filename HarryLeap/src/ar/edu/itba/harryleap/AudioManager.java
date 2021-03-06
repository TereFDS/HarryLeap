package ar.edu.itba.harryleap;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioManager {
	static String SP_SOUND_PATH = "resources/leviosa_spell_sound";
	static String SP_SOUND_EXTENSION = ".wav";
	AudioInputStream  spellSound = null;

	int bg_index_image = 0;
	int fPosX = 0;
	int fPosY = 0;

	public AudioManager() {

		try {

			spellSound = AudioSystem.getAudioInputStream(new File(SP_SOUND_PATH
					+ SP_SOUND_EXTENSION));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void playSpellSound(){
		Thread t =new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					try {
			            Clip clip = AudioSystem.getClip();
			            clip.open(spellSound);
			            try {
			                clip.start();
			                try {
			                    Thread.sleep(100);
			                } catch (InterruptedException e) {
			                    e.printStackTrace();
			                }
			                clip.drain();
			            } finally {
			                clip.close();
			            }
			        } catch (LineUnavailableException e) {
			            e.printStackTrace();
			        } finally {
			            spellSound.close();
			        }
				}catch (Exception e){
			        e.printStackTrace();
				}			
			}
		});
		t.start();
	}
	
}
