package com.example.stylesimplified.backend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddOutfitController {
    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        navigateTo(event, "outfits-view.fxml", "My Outfits");
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
