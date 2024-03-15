package com.mirohaap.towerofhanoitutor;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.util.ArrayList;
import java.util.Stack;


public class Tutor {
    //dummy tutor class, all moves are validated
    private static Tutor _instance;
    private boolean enabled;
    private ArrayList<Move> bestMoves = new ArrayList<Move>();

    private int moveNumber = 0;

    private volatile boolean isSpeaking = false;


    Voice voice;

    private Tutor() {
        // TODO: Cache this result to avoid load times.
        initializeVoice();
        enabled = true;

    }

    public void calculateMoves(int numRings) {
        computeBestMoves(numRings, 1, 3, 2);
    }

    private void initializeVoice() {
        VoiceManager voiceManager = VoiceManager.getInstance();
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = voiceManager.getVoice("kevin16");
        voice.allocate();
        voice.setRate(110);
        voice.setPitch(100);
        voice.setVolume(0.8f);
    }


    public void speak(String message) {
        if (isSpeaking) {
            return;
        }

        new Thread(() -> {
            try {
                isSpeaking = true;
                voice.speak(message);
            } finally {
                isSpeaking = false;
            }
        }).start();
    }

    public boolean isBestMove(Move move) {
        if (!enabled) {
            return true;
        }
        if (!move.equals(bestMoves.get(moveNumber))) {
            speak(bestMoves.get(moveNumber).toString());
            return false;
        }

        moveNumber += 1;
        return true;
    }

    public boolean validateMove(Move move) {

        if (bestMoves.isEmpty()) {
            throw new RuntimeException("Tutor validation called before calculateMoves called!");
        }

        if (!enabled) {
            move.setValid(true);
            return true;
        }

        boolean moveStatus = isBestMove(move);
        move.setValid(moveStatus);
        return moveStatus;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        speak("Im here to help!");
        enabled = true;
    }

    public void addBestMove(int n, int from, int to) {
        Move move = new Move(n, from, to);
        bestMoves.add(move);
    }

    private void computeBestMoves(int n, int from_rod, int to_rod, int aux_rod) {
        if (n == 1) {
            addBestMove(1, from_rod, to_rod);
            return;
        }
        computeBestMoves(n - 1, from_rod, aux_rod, to_rod);
        addBestMove(n, from_rod, to_rod);
        computeBestMoves(n - 1, aux_rod, to_rod, from_rod);
    }

    public static Tutor getInstance() {
        if (_instance == null) {
            _instance = new Tutor();
        }
        return _instance;
    }
}