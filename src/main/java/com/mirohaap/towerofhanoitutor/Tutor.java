package com.mirohaap.towerofhanoitutor;

public class Tutor {
    //dummy tutor class, all moves are validated
    private static Tutor _instance;
    private boolean enabled;
    private Tutor(){
        enabled = true;
    }

    public boolean validateMove(Move move){
        if(enabled){
            //perform calculations
            //if wrong move, setValid(false) and return false
            move.setValid(false);
            return false;
        }
        move.setValid(true);
        return true;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void disable(){
        enabled = false;
    }

    public void enable(){
        enabled = true;
    }

    public static Tutor getInstance(){
        if(_instance == null){
            _instance = new Tutor();
        }
        return _instance;
    }
}
