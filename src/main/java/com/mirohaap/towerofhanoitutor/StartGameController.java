package com.mirohaap.towerofhanoitutor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class StartGameController {
    @FXML
    private Button startButton;
    @FXML
    private Spinner ringCounter;
    @FXML
    private CheckBox tutorCheckBox;
    private Window window;

    @FXML
    private void initialize() throws IOException {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10, 6, 1);
        ringCounter.setValueFactory(valueFactory);
        valueFactory.setWrapAround(true);
    }


    @FXML
    public void setWindow(Window window){
        this.window = window;
    }

    @FXML
    private void startGameClicked() throws IOException {
        if (tutorCheckBox.isSelected()) {
            Tutor.getInstance().enable();
        } else {
            Tutor.getInstance().disable();
        }
        SoundPlayer.getInstance();
        Stage gameStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 809, 642);
        GameController controller = fxmlLoader.getController();
        controller.setWindow(window);
        controller.textToDisplay("I'm here to help!");

        gameStage.setTitle("Tower of Hanoi");
        gameStage.setScene(scene);
        gameStage.setResizable(false);
        gameStage.show();
        int numRings = (int) ringCounter.getValue();
        controller.initRings(numRings);
        Tutor.getInstance().setController(controller);
        Tutor.getInstance().calculateMoves(numRings);

        Stage current = (Stage) startButton.getScene().getWindow();
        current.close();
    }

}
