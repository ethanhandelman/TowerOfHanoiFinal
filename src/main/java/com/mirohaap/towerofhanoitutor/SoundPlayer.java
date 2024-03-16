package com.mirohaap.towerofhanoitutor;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private static SoundPlayer instance;
    private Clip[] placeClips;
    private Clip wrongClip;
    private int currentPlaceIndex;

    private SoundPlayer() {
        // Load place sound files
        placeClips = new Clip[4];
        for (int i = 0; i < 4; i++) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("place" + (i + 1) + ".wav"));
                placeClips[i] = AudioSystem.getClip();
                placeClips[i].open(audioInputStream);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        // Load wrong sound file
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("wrong.wav"));
            wrongClip = AudioSystem.getClip();
            wrongClip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        currentPlaceIndex = 0;
    }

    public static SoundPlayer getInstance() {
        if (instance == null) {
            instance = new SoundPlayer();
        }
        return instance;
    }

    public void playPlace() {
        Clip clip = placeClips[currentPlaceIndex];
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
        currentPlaceIndex = (currentPlaceIndex + 1) % placeClips.length;
    }

    public void playWrong() {
        if (wrongClip.isRunning()) {
            wrongClip.stop();
        }
        wrongClip.setFramePosition(0);
        wrongClip.start();
    }

    // Additional methods can be added for playing other sound effects

}
