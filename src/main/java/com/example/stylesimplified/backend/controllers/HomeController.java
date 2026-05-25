package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    void handleWardrobeButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "wardrobe-view.fxml", "My Wardrobe");
    }

    @FXML
    void handleTagsButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "tags-view.fxml", "My Tags");
    }

    @FXML
    void handleOutfitsButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "outfits-view.fxml", "My Outfits");
    }

    @FXML
    void handleRecommendationsButton(ActionEvent event) {
        System.out.println("Recommendations button clicked - to be implemented.");
    }

}
