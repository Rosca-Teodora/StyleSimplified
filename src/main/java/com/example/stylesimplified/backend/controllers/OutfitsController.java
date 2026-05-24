package com.example.stylesimplified.backend.controllers;

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
        navigateTo(event, "add-outfit-view.fxml", "Add outfit");
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        navigateTo(event, "home-view.fxml", "Style Simplified - Home");
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
