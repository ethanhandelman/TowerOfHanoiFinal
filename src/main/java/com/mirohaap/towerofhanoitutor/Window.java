package com.mirohaap.towerofhanoitutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Window extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 809, 642);
        GameController controller = (GameController) fxmlLoader.getController();

        stage.setTitle("Tower of Hanoi");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        controller.initRings(5);*/
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("start-game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 164, 150);
        stage.setTitle("Tower of Hanoi - New Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}