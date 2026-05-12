package com.example.stylesimplified;

import com.example.stylesimplified.backend.services.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DatabaseManager.setupDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Style Simplified - Home");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        System.out.println("App and database closing ");
        DatabaseManager.closeConnection();
    }

    public static void main(String[] args){
        launch();
    }
}
