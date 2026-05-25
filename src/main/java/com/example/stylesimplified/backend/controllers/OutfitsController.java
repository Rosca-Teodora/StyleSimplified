package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.models.Outfit;
import com.example.stylesimplified.backend.services.WardrobeService;
import com.example.stylesimplified.backend.utils.SceneManager;
import com.example.stylesimplified.backend.utils.UIFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

public class OutfitsController {

    @FXML
    private FlowPane outfitPane;
    private final WardrobeService service = WardrobeService.getInstance();

    @FXML
    public void initialize() {
        populateOutfits();
    }

    private void populateOutfits(){
        outfitPane.getChildren().clear();

        Set<Outfit> allOutfits = service.getWardrobe().getAllOutfits();
        for (Outfit outfit : allOutfits){
            VBox card = UIFactory.createOutfitCard(outfit);

            card.setOnMouseClicked(e -> {
                try {
                    SceneManager.navigateTo(new ActionEvent(card, card), "outfit-details-view.fxml", outfit.getName() + " outfit details");
                }
                catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            outfitPane.getChildren().add(card);
        }
    }

    @FXML
    void handleAddOutfitButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "add-outfit-view.fxml", "Add outfit");
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "home-view.fxml", "Style Simplified - Home");
    }
}
