package com.mirohaap.towerofhanoitutor;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController implements PropertyChangeListener {

    @FXML
    private AnchorPane gamePanel;
    @FXML
    private Slider speedSlider;
    @FXML
    private Text secondsDisplay, timeLabel;
    @FXML
    private Button autoPlayButton, backButton, nextButton;
    @FXML
    public TextFlow tutorText;

    TranslateTransition currentTransition;
    private DragDropUtil dragDropUtil;
    private AutoPlayUtil autoPlayUtil;

    private Window window;

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
        Repository.getInstance().addListener(this);
    }

    @FXML
    public void onRestartButtonClick() {
        // Close current Game Window
        Stage currentGameStage = (Stage) autoPlayButton.getScene().getWindow();
        currentGameStage.close();

        // Open initial game setup window

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
//    public void stepForward(){
//        if(currentTransition != null && currentTransition.getStatus() == Animation.Status.RUNNING){
//            return;
//        }
//        dragDropUtil.disableUserInput();
//        Move next = Tutor.getInstance().getNextMove();
//        next.setValid(true);
//        Repository.getInstance().applyMove(next);
//        currentTransition = dragDropUtil.animateMove(next, speedSlider.getValue() * 1000 * 0.9, new MutableBoolean(true));
//
//
////        if(Repository.getInstance().checkWin()){
////            System.out.println("Autoplay won!");
////            allowInteractions(false);
////            autoPlayButton.setText("AutoPlay");
////            autoPlayButton.setDisable(true);
////            dragDropUtil.disableUserInput();
//        }
//    }
//
////    @FXML
////    public void stepBack(){
////        if(currentTransition != null && currentTransition.getStatus() == Animation.Status.RUNNING){
////            return;
////        }
////        dragDropUtil.disableUserInput();
////        Move last = Repository.getInstance().popLastValidMove();
////        Tutor.getInstance().revertMove();
////
////        currentTransition = dragDropUtil.animateMove(last.reversed(), speedSlider.getValue() * 1000 * 0.9, new MutableBoolean(true));
////
////    }

    public void propertyChange(PropertyChangeEvent evt){
        System.out.println("move made" + (Move) evt.getNewValue());
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

    public void textToDisplay(String message){
        tutorText.getChildren().clear();
        Text text = new Text(message);
        text.setFont(Font.font(20));
        tutorText.getChildren().add(text);
    }

    public void setWindow(Window window){
        this.window = window;
    }


    @FXML
    public void onRestartButtonCLick() throws IOException{
        window.resetGame();
    }

}