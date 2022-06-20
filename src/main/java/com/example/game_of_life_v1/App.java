package com.example.game_of_life_v1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("hello-view.fxml"));
        Scene scene = fxmlLoader.load();
        mainStage = stage;
        mainStage.setTitle("Game of Life v1");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}