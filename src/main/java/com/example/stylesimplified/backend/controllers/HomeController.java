package com.example.stylesimplified.backend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    void handleWardrobeButton(ActionEvent event) throws IOException {
        navigateTo(event, "wardrobe-view.fxml", "My Wardrobe");
    }

    @FXML
    void handleTagsButton(ActionEvent event) throws IOException {
        navigateTo(event, "tags-view.fxml", "My Tags");
    }

    @FXML
    void handleOutfitsButton(ActionEvent event) {
        System.out.println("Outfits button clicked - to be implemented.");
        // Aici vei adăuga navigarea către outfits-view.fxml când va fi gata
    }

    @FXML
    void handleRecommendationsButton(ActionEvent event) {
        System.out.println("Recommendations button clicked - to be implemented.");
    }

    private void navigateTo(ActionEvent event, String fxmlFile, String title) throws IOException {
        String absoluteFxmlPath = "/com/example/stylesimplified/" + fxmlFile;
        Parent root = FXMLLoader.load(getClass().getResource(absoluteFxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}
