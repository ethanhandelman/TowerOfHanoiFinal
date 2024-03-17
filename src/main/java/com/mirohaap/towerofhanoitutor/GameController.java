package com.mirohaap.towerofhanoitutor;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    @FXML
    private AnchorPane gamePanel;
    @FXML
    private Slider speedSlider;
    @FXML
    private Text secondsDisplay, timeLabel;
    @FXML
    private Button autoPlayButton, backButton, nextButton;

    TranslateTransition currentTransition;
    private DragDropUtil dragDropUtil;
    private AutoPlayUtil autoPlayUtil;

    @FXML
    private void initialize() {
        if(!Tutor.getInstance().isEnabled()){
            speedSlider.setVisible(false);
            autoPlayButton.setVisible(false);
            backButton.setVisible(false);
            nextButton.setVisible(false);
            secondsDisplay.setVisible(false);
            timeLabel.setVisible(false);
        }

        secondsDisplay.textProperty().bind(
                Bindings.format(
                "%.2f",
                speedSlider.valueProperty()
        ));

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

    @FXML
    public void beginAutoPlay(){
        if(autoPlayUtil == null){
            //autoPlayButton.setDisable(true);
            allowInteractions(false);
            autoPlayUtil = new AutoPlayUtil(dragDropUtil);
            autoPlayUtil.beginPlaying((int) (speedSlider.getValue() * 1000));
            autoPlayButton.setText("Pause");
        }
        else{
            autoPlayUtil.stopPlaying();
            autoPlayUtil = null;
            allowInteractions(true);
            autoPlayButton.setText("AutoPlay");
        }

    }

    @FXML
    public void stepForward(){
        if(currentTransition != null && currentTransition.getStatus() == Animation.Status.RUNNING){
            return;
        }
        dragDropUtil.disableUserInput();
        Move next = Tutor.getInstance().getNextMove();
        next.setValid(true);
        Repository.getInstance().applyMove(next);
        currentTransition = dragDropUtil.animateMove(next, speedSlider.getValue() * 1000 * 0.9, new MutableBoolean(true));


        if(Repository.getInstance().checkWin()){
            System.out.println("Autoplay won!");
            allowInteractions(false);
            autoPlayButton.setText("AutoPlay");
            autoPlayButton.setDisable(true);
            dragDropUtil.disableUserInput();
        }
    }

    @FXML
    public void stepBack(){
        if(currentTransition != null && currentTransition.getStatus() == Animation.Status.RUNNING){
            return;
        }
        dragDropUtil.disableUserInput();
        Move last = Repository.getInstance().popLastValidMove();
        Tutor.getInstance().revertMove();
        currentTransition = dragDropUtil.animateMove(last, speedSlider.getValue() * 1000 * 0.9, new MutableBoolean(true));

    }

    private void allowInteractions(boolean canInteract){
        speedSlider.setDisable(!canInteract);
        backButton.setDisable(!canInteract);
        nextButton.setDisable(!canInteract);
    }

    public void gameComplete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You won the game!", ButtonType.FINISH);
        alert.showAndWait();
    }


}