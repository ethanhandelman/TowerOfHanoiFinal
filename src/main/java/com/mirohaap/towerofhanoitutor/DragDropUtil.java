package com.mirohaap.towerofhanoitutor;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DragDropUtil {
    private List<Ring> rings;
    private AnchorPane gamePane;
    private List<SnapRange> tops;
    private Rectangle projection;
    private static final double TOWER_BOTTOM_Y = 360, TOWER_1_CENTER = 128, TOWER_2_CENTER = 364, TOWER_3_CENTER = 600, DEFAULT_RANGE = 50;
    private static final SnapRange TOWER_1_BOTTOM = new SnapRange(TOWER_1_CENTER, TOWER_BOTTOM_Y, DEFAULT_RANGE, 1),
            TOWER_2_BOTTOM = new SnapRange(TOWER_2_CENTER, TOWER_BOTTOM_Y, DEFAULT_RANGE, 2),
            TOWER_3_BOTTOM = new SnapRange(TOWER_3_CENTER, TOWER_BOTTOM_Y, DEFAULT_RANGE, 3);
    private double startX, startY, offsetX, offsetY;

    public DragDropUtil(AnchorPane gamePane, List<Ring> rings){
        this.gamePane = gamePane;
        this.rings = rings;
        for(Ring ring : rings){
            makeRingDraggable(ring);
        }
        //this assumes the game starts with all rings on tower 1
        tops = new ArrayList<>();
        refreshTops();

    }

    private void makeRingDraggable(Ring ring){
        StackPane ringPane = ring.getVisualRing();
        ringPane.setOnMousePressed(e -> {
            if(!Repository.getInstance().isTop(ring.getNum())){
                return;
            }

            ringPane.setCursor(Cursor.CLOSED_HAND);
            startX = ringPane.getLayoutX();
            startY = ringPane.getLayoutY();
            offsetX = e.getSceneX() - ringPane.getLayoutX();
            offsetY = e.getSceneY() - ringPane.getLayoutY();
        });

        ringPane.setOnMouseDragged(e -> {
            if(!Repository.getInstance().isTop(ring.getNum())){
                return;
            }

            ringPane.setLayoutX(e.getSceneX() - offsetX);
            ringPane.setLayoutY(e.getSceneY() - offsetY);

            SnapRange inRange = checkSnapRanges(ring);
            clearProjection();
            if(inRange != null){
                if(!inRange.hasOwner() || inRange.getOwner().getNum() > ring.getNum()){ //checks if ring is valid to be placed
                    projectRect(ringPane, inRange, Color.BLACK);
                }
                else{
                    projectRect(ringPane, inRange, Color.RED);
                }
            }
        });

        ringPane.setOnMouseReleased(e -> {
            if(!Repository.getInstance().isTop(ring.getNum())){
                return;
            }

            clearProjection();
            SnapRange inRange = checkSnapRanges(ring);
            Move made = null;
            if(inRange != null && (!inRange.hasOwner() || inRange.getOwner().getNum() > ring.getNum())){
                ringPane.setCursor(Cursor.DEFAULT);
                made = new Move(ring.getNum(), Repository.getInstance().getTower(ring.getNum()), inRange.getTower());
                if(Tutor.getInstance().validateMove(made)){

                    ringPane.setLayoutX(inRange.getOgX() - (ringPane.getWidth() / 2));
                    ringPane.setLayoutY(inRange.getOgY() - ringPane.getHeight() + 1);

                    refreshCursors(made);
                }

                Repository.getInstance().applyMove(made);
                refreshTops();
            }

            if(made == null || !made.isValid()){
                ringPane.setCursor(Cursor.OPEN_HAND);
                ringPane.setLayoutX(startX);
                ringPane.setLayoutY(startY);
            }

            System.out.println(tops);
        });
    }

    private void refreshCursors(Move move){
        refreshCursors(Repository.getInstance().getTowerByIndex(move.getTo() - 1));
        refreshCursors(Repository.getInstance().getTowerByIndex(move.getFrom() - 1));
    }

    private void refreshCursors(List<Integer> toRefresh){
        for(Integer i : toRefresh){
            Ring ring = rings.get(i - 1);
            if(!ring.equals(toRefresh.getLast())){
                ring.getVisualRing().setCursor(Cursor.DEFAULT);
            }
        }
    }

    private void refreshTops(){
        tops.clear();
        System.out.println(tops);
        List<Integer> intTops = Repository.getInstance().getTops();
        System.out.println(intTops);
        for(int i = 0; i < 3; i++){
            System.out.println("adding top" + i);
            if(intTops.get(i) == -1){
                switch(i){
                    case 0: tops.add(TOWER_1_BOTTOM); break;
                    case 1: tops.add(TOWER_2_BOTTOM); break;
                    case 2: tops.add(TOWER_3_BOTTOM); break;
                    default : break;
                }
            }
            else{
                Ring top = rings.get(intTops.get(i) - 1);
                top.getVisualRing().setCursor(Cursor.OPEN_HAND);
                tops.add(new SnapRange(top.getVisualRing().getLayoutX() + (top.getVisualRing().getWidth() / 2), top.getVisualRing().getLayoutY(), DEFAULT_RANGE, top));
            }
        }
    }

    private SnapRange checkSnapRanges(Ring ring){
        for(SnapRange top : tops){
            if(!top.isOwner(ring) &&top.inRange(ring.getVisualRing().getLayoutX() + (ring.getVisualRing().getWidth() / 2), ring.getVisualRing().getLayoutY() + (ring.getVisualRing().getHeight() / 2))){
                //System.out.println("In range");
                return top;
            }
        }
        return null;
    }

    private void clearProjection() {
        if(projection != null){
            projection.setVisible(false);
            gamePane.getChildren().remove(projection);
        }
        projection = null;
    }

    //example is the rectangle to show the outline of, sr is where it will be projected above
    public void projectRect(StackPane example, SnapRange sr, Color color){
        projection = new Rectangle(sr.getOgX() - (example.getWidth() / 2), sr.getOgY() - example.getHeight(), example.getWidth(), example.getHeight());
        projection.setFill(Color.TRANSPARENT);
        projection.setStroke(color);
        projection.setStrokeWidth(3);
        projection.setVisible(true);
        gamePane.getChildren().add(projection);
    }
}
