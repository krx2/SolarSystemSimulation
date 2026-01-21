package com.example.solarsystemsimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Główna klasa aplikacji symulacji układu słonecznego
 */
public class SolarSystemApplication extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            SolarSystemApplication.class.getResource("simulation-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1400, 850);
        stage.setTitle("Symulacja Układu Słonecznego");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
