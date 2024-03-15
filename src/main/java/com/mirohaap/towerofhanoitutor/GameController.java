package com.mirohaap.towerofhanoitutor;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameController {
    @FXML
    private AnchorPane gamePanel;
    private DragDropUtil dragDropUtil;
    private boolean tutorEnabled;


    @FXML
    private void initialize() {


    }

    public void initRings(int ringCount) {
        //keep only the rings that are needed
        List<Ring> rings = new ArrayList<>() {{
            for (int i = 1; i <= 10; i++) {
                if (i <= ringCount) {
                    Ring cur = new Ring((StackPane) gamePanel.lookup("#ring" + i), i);
                    cur.getVisualRing().setLayoutY(cur.getVisualRing().getLayoutY() + (29 * (10 - ringCount)));
                    add(cur);
                } else {
                    gamePanel.getChildren().remove(gamePanel.lookup("#ring" + i));
                }
            }
        }};
        Repository.getInstance().init(ringCount);

        //adjust tower heights based off ring count
        if (ringCount < 10) {
            double adjustment = 29 * (10 - ringCount);
            for (int i = 1; i < 4; i++) {
                Rectangle cur = (Rectangle) gamePanel.lookup("#tower" + i);
                cur.setHeight(cur.getHeight() - adjustment);
                cur.setLayoutY(cur.getLayoutY() + adjustment);
            }
        }

        this.dragDropUtil = new DragDropUtil(gamePanel, rings);
    }


    @FXML
    public void onTutorToggled() {

    }

    @FXML
    public void onRestartButtonClick() {

    }


}