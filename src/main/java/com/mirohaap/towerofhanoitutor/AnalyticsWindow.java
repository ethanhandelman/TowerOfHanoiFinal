package com.mirohaap.towerofhanoitutor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class AnalyticsWindow {

    @FXML
    private Text tutorText, optimalText, unoptimalText, timeText;

//    NumberAxis xAxis = new NumberAxis();
//    NumberAxis yAxis = new NumberAxis();

    @FXML
    LineChart<Number, Number> pastMovesChart;

    @FXML
    private void initialize() {

        if (!Tutor.getInstance().isEnabled()) {
            tutorText.setText("Enable tutor to get optimal move data.");
        } else {
            optimalText.setText("Total optimal moves: " + AnalyticsUtil.getInstance().getNumberOfOptimalMoves());
            unoptimalText.setText("Total un-optimal moves: " + AnalyticsUtil.getInstance().getNumberOfUnoptimalMoves());
        }
        timeText.setText("Total time spent: " + AnalyticsUtil.getInstance().getElapsedTime() + " seconds");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        ArrayList<Integer> moves = AnalyticsUtil.getInstance().getOptimalMovesOverTime();
        series.setName("Number of Optimal Moves Over Time");
        for (int i = 0; i < moves.size(); i++) {
            series.getData().add(new XYChart.Data(i, moves.get(i)));
        }
        pastMovesChart.getData().add(series);
    }



    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("analytics-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        Stage secondStage = new Stage();
        secondStage.setTitle("Game Analytics");
        secondStage.setScene(scene);
        secondStage.setResizable(false);
        secondStage.setOnCloseRequest(event -> handleCloseBehavior());
        secondStage.show();
    }

    public void handleCloseBehavior() {
        AnalyticsUtil.getInstance().logOptimalMoves();
        AnalyticsUtil.getInstance().writeAnalyticDataToFile();
    }
}
