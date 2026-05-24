package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.services.WardrobeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class AddOutfitController {

    private WardrobeService service = WardrobeService.getInstance();

    @FXML
    public void initialize() {
        populateGalery();
    }

    private void populateGalery(){
        Set<ClothingItem> allClothes = service.getWardrobe().getOwnedClothes();

    }

    @FXML
    private void handleSaveOutfitButton(ActionEvent event) {

    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
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
