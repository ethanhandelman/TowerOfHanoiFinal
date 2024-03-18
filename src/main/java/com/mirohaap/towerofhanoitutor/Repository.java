package com.mirohaap.towerofhanoitutor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Repository{
    private static Repository _instance;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    //for each tower, [0] is the bottom, last index is the top
    private List<List<Integer>> towers;
    private Stack<Move> moves;
    private boolean initialized;

    private Repository(){
        towers = new ArrayList<>();
        moves = new Stack<>();
        initialized = false;
    }

    public void addListener(PropertyChangeListener listener){
        changes.addPropertyChangeListener(listener);
    }

    public void init(int ringCount){
        towers.clear();
        for(int i = 0; i < 3; i++){
            towers.add(new ArrayList<>());
        }
        for(int i = ringCount; i > 0; i--){
            towers.getFirst().add(i);
        }
        initialized = true;
    }

    public List<Integer> getTowerByIndex(int index){
        if(index < towers.size() && index >= 0){
            return List.copyOf(towers.get(index));
        }
        throw new IndexOutOfBoundsException("Towers must be referenced using indexes 0, 1, or 2.");
    }

    public List<Integer> getTops(){
        List<Integer> tops = new ArrayList<>();
        for(List<Integer> tower : towers){
            if(tower.isEmpty()){
                tops.add(-1);
            }
            else{
                tops.add(tower.getLast());
            }
        }
        return tops;
    }

    public void applyMove(Move move){
        if(move.isValid()){
            towers.get(move.getFrom() - 1).remove((Integer) move.getN());
            towers.get(move.getTo() - 1).add(move.getN());
        }
        logMove(move);
    }

    public boolean checkWin(){
        return (towers.getFirst().isEmpty() && towers.getLast().isEmpty()) || (towers.getFirst().isEmpty() && towers.get(1).isEmpty());
    }

    private void logMove(Move move){
        moves.push(move);
        System.out.println(towers);
        changes.firePropertyChange("move", null, move);
    }

    public Move popLastValidMove(){
        Move move;
        do {
            if(moves.isEmpty()){
                throw new RuntimeException("No valid moves have been logged yet!");
            }
            move = moves.pop();
        } while(!move.isValid());

        System.out.println("Popped " + move);
        towers.get(move.getTo() - 1).removeLast();
        towers.get(move.getFrom() - 1).add(move.getN());

        System.out.println(towers);
        return move;
    }

    public int getTotalMoveCount(){
        return moves.size();
    }

    public int getValidMoveCount(){
        return (int) moves.stream().filter(Move::isValid).count();
    }

    public int getInvalidMoveCount(){
        return (int) moves.stream().filter(m -> !m.isValid()).count();
    }

    public boolean isTop(Integer test){
        for(List<Integer> tower : towers){
            if(!tower.isEmpty() && tower.getLast().equals(test)){
                return true;
            }
        }
        return false;
    }

    public int getTower(Integer num){
        for(int i = 0; i < 3; i++){
            for(Integer j : towers.get(i)){
                if(j == num){
                    return i + 1;
                }
            }
        }
        return -1;
    }

    public static Repository getInstance(){
        if(_instance == null){
            _instance = new Repository();
        }
        else if(!_instance.initialized){
            throw new RuntimeException("Repository accessed before being initialized");
        }
        return _instance;
    }
}
