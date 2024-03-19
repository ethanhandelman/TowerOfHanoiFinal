package com.mirohaap.towerofhanoitutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Window extends Application {
    private Stage primaryStage;

    private static Window _window;

    @Override
    public void start(Stage primaryStage) throws IOException{
        this.primaryStage = primaryStage;
        _window = this;
        showStartScreen();

    }

    public static Window getInstance() {
        return _window;
    }

    public void showStartScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource("start-game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 164, 150);
        primaryStage.setTitle("Tower of Hanoi - New Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void resetGame() throws IOException{
        showStartScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}