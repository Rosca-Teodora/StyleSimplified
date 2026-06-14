package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.commands.Command;
import com.example.stylesimplified.backend.commands.CommandInvoker;
import com.example.stylesimplified.backend.commands.RemoveOutfitCommand;
import com.example.stylesimplified.backend.models.*;
import com.example.stylesimplified.backend.services.WardrobeService;
import com.example.stylesimplified.backend.utils.DataInitializable;
import com.example.stylesimplified.backend.utils.OutfitNavigationContext;
import com.example.stylesimplified.backend.utils.SceneManager;
import com.example.stylesimplified.backend.utils.UIFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutfitDetailsController implements DataInitializable<OutfitNavigationContext> {

    private Outfit currentOutfit;
    private final CommandInvoker cmdInvoker = new CommandInvoker();

    @FXML private Label outfitNameLabel;
    @FXML private GridPane pinterestBoard;
    private Map<String, FlowPane> anatomicalSlots = new HashMap<>();

    @FXML
    public void initialize() {
        buildAnatomicalGrid();
    }

    @Override
    public void initData(OutfitNavigationContext context) {
        this.currentOutfit = context.getOutfit();
        outfitNameLabel.setText(currentOutfit.getName());
        populatePinterestBoard();
    }

    private void buildAnatomicalGrid() {
        pinterestBoard.getChildren().clear();
        anatomicalSlots.clear();

        String[] requiredSlots = { // helper de pozitie
                "1,0", // center-head
                "1,1", // center-torso ( pt Tops)
                "0,1", // left-torso (pt Hands/Wrists)
                "2,1", // right-torso
                "1,2", // center-legs (pt Bottoms)
                "1,3"  // center-feet
        };

        for (String slot : requiredSlots) {
            String[] coords = slot.split(",");
            int col = Integer.parseInt(coords[0]);
            int row = Integer.parseInt(coords[1]);

            // flow pane setup pt tipurile de haine din acelasi tip care satu una dupa alta
            FlowPane cellPane = new FlowPane();
            cellPane.setAlignment(javafx.geometry.Pos.CENTER);
            cellPane.setHgap(15.0);
            cellPane.setVgap(15.0);

            pinterestBoard.add(cellPane, col, row);
            anatomicalSlots.put(slot, cellPane);
        }
    }

    private void populatePinterestBoard() {
        for (FlowPane slot : anatomicalSlots.values()) {
            slot.getChildren().clear();
        }

        for (ClothingItem item : currentOutfit.getClothes()) {
            ImageView node = UIFactory.loadClothingImage(item.getImgPath(), 130, 130);

            String slotKey = "2,1"; // Safe fallback
            if (item instanceof Top) slotKey = "1,1";
            else if (item instanceof Bottom) slotKey = "1,2";
            else if (item instanceof Accessory) {
                String placement = ((Accessory) item).getPlacement().toLowerCase();
                switch (placement) { // astea sunt hardcodate momentan, canda o sa schimb modalitate de introducere a pozitiei unei accesorii
                    case "head": slotKey = "1,0"; break;
                    case "feet": slotKey = "1,3"; break;
                    case "hands/wrists": slotKey = "0,1"; break;
                    case "neck": slotKey = "1,1"; break;
                }
            }

            FlowPane targetPane = anatomicalSlots.get(slotKey);
            if (targetPane != null) targetPane.getChildren().add(node);
        }
    }

    @FXML
    void handleEditMode(ActionEvent event) {
        // tranzitie la outfit view
        OutfitNavigationContext context = new OutfitNavigationContext(currentOutfit, true);
        SceneManager.navigateToWithData((Node) event.getSource(), "add-outfit-view.fxml", "Edit Outfit", context);
    }

    @FXML
    void handleRemoveOutfit(ActionEvent event) throws IOException {
        Command removeOutfit = new RemoveOutfitCommand(WardrobeService.getInstance(), currentOutfit);
        cmdInvoker.executeCommand(removeOutfit);

        SceneManager.navigateTo(event, "outfits-view.fxml", "My Outfits");

    }

    @FXML
    void handleBack(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "outfits-view.fxml", "My Outfits");
    }
}