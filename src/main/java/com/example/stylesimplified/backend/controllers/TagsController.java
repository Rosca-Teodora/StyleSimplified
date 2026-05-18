package com.example.stylesimplified.backend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class TagsController {
    @FXML
    private TextField nameField;

    @FXML
    void handleAddTagButton(ActionEvent event) throws IOException {
        String name = nameField.getText();

        if (name.isEmpty()) {;
            return;
        }


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
