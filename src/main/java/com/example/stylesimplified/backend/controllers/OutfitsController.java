package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.models.Outfit;
import com.example.stylesimplified.backend.services.WardrobeService;
import com.example.stylesimplified.backend.utils.OutfitNavigationContext;
import com.example.stylesimplified.backend.utils.SceneManager;
import com.example.stylesimplified.backend.utils.UIFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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

            card.setCursor(Cursor.HAND);
            card.setOnMouseClicked(e -> {
                OutfitNavigationContext context = new OutfitNavigationContext(outfit, false); // nu incepe in edit mode
                SceneManager.navigateToWithData((Node) e.getSource(), "outfit-details-view.fxml", "Fit details", context);
            });

            outfitPane.getChildren().add(card);
        }
    }

    @FXML
    void handleAddOutfitButton(ActionEvent event) throws IOException {
        // desi nu trimit data neaparat catre view am facut AddutfitController ul sa fie dual si pt editare si pt add pt ca nu voiam sa scriu de 2 ori acelasi cod
        SceneManager.navigateToWithData((Node) event.getSource(), "add-outfit-view.fxml", "Add Outfit", null);
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "home-view.fxml", "Style Simplified - Home");
    }
}
