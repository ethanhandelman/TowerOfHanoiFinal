package com.mirohaap.towerofhanoitutor;

public class SnapRange {
    private final double ogX, ogY;
    private double maxX, maxY, minX, minY;
    private static final int DEFAULT_SNAP_RANGE = 100;
    private Ring owner;
    private int tower;

    /**
     * Constructor for SnapRange with specified x, y, and range.
     *
     * @param x The original X-coordinate.
     * @param y The original Y-coordinate.
     * @param range The snap range.
     */
    public SnapRange(double x, double y, double range, int tower){
        ogX = x;
        ogY = y;
        setRange(range);
        owner = null;
        this.tower = tower;
    }

    @Override
    public String toString() {
        return "SnapRange{" +
                "ogX=" + ogX +
                ", ogY=" + ogY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", minX=" + minX +
                ", minY=" + minY +
                ", owner=" + owner +
                ", tower=" + tower +
                '}';
    }

    public SnapRange(double x, double y, double range, Ring owner){
        this(x, y, range, Repository.getInstance().getTower(owner.getNum()));
        this.owner = owner;
    }



    /**
     * Checks if the given coordinates are within the snap range.
     *
     * @param x The X-coordinate.
     * @param y The Y-coordinate.
     * @return True if within snap range, false otherwise.
     */
    public boolean inRange(double x, double y){
        return x < maxX && y < maxY && x > minX && y > minY;
    }

    /**
     * Sets the snap range based on the specified range.
     *
     * @param range The snap range.
     */
    public void setRange(double range){
        maxX = ogX + range;
        maxY = ogY + range;
        minX = ogX - range;
        minY = ogY - range;
    }

    public double getOgX(){
        return ogX;
    }

    public double getOgY(){
        return ogY;
    }

    public boolean hasOwner(){
        return owner != null;
    }

    public boolean isOwner(Ring ring){
        return ring == owner;
    }

    public Ring getOwner(){
        return owner;
    }

    public int getTower(){
        return tower;
    }


}
