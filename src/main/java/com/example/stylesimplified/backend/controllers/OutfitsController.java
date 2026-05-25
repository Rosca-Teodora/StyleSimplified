package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class OutfitsController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void handleAddOutfitButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "add-outfit-view.fxml", "Add outfit");
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "home-view.fxml", "Style Simplified - Home");
    }
}
