package com.mirohaap.towerofhanoitutor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AnalyticsWindow {

    @FXML
    private Text tutorText, optimalText, unoptimalText, timeText;

    @FXML
    private void initialize() {
        if (!Tutor.getInstance().isEnabled()) {
            tutorText.setText("Enable tutor to get optimal move data.");
        } else {
            optimalText.setText("Total optimal moves: " + AnalyticsUtil.fetchNumberOfMoves(true));
            unoptimalText.setText("Total un-optimal moves: " + AnalyticsUtil.fetchNumberOfMoves(false));
        }
        timeText.setText("Total time spent: " + AnalyticsUtil.fetchInGameTime() + " seconds");

    }

    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("analytics-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        Stage secondStage = new Stage();
        secondStage.setTitle("Game Analytics");
        secondStage.setScene(scene);
        secondStage.setResizable(false);
        secondStage.show();

    }
}
