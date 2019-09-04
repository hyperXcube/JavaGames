package games.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// Handles audio
public class Sound {
    public static void play(String filename, String game) {    
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("assets\\" + game + "\\" + filename + ".wav"));
            Clip c = AudioSystem.getClip();
            c.open(ais);
            c.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}