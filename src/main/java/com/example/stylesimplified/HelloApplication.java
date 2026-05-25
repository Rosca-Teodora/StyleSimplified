package com.example.stylesimplified;

import com.example.stylesimplified.backend.services.DatabaseManager;
import com.example.stylesimplified.backend.services.WardrobeService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DatabaseManager.setupDatabase();

        WardrobeService.getInstance().loadItemsFromDb(); // load actual saved clothes

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Style Simplified - Home");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/app_icon.png")));
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
