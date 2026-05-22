package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.services.WardrobeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

import static com.example.stylesimplified.backend.utils.UIFactory.*;

public class WardrobeController {

    private WardrobeService service = WardrobeService.getInstance();

    // TilePane from current scene (list of clothes)
    @FXML
    private TilePane clothesCard;

    private HBox createClothingUI(ClothingItem ci) {
        HBox box = createHBox();
        box.getStyleClass().add("clothing_item");
        box.setSpacing(40);

        ImageView clothingView = new ImageView();
        Label label = new Label(ci.getName()); // label-ul imaginii

        // adauga imaginea propriu-zisa in img view ul pt clothing item
        try {
            File imageFile = new File(ci.getImgPath());
            if (imageFile.exists()){
                clothingView.setImage(new Image(imageFile.toURI().toString()));
            }
            else {
                System.err.println("Image not found for " + ci.getName() + " at path: " + ci.getImgPath());
                // Optional: Set a placeholder image
                // imgView.setImage(new javafx.scene.image.Image("/path/to/placeholder.png"));
            }
        } catch (Exception e) {
            System.err.println("Failed to load image for " + ci.getName() + ": " + e.getMessage());
        }
        clothingView.setFitWidth(200);
        clothingView.setFitHeight(200);
        clothingView.setPreserveRatio(true);

        Button editBtn = createButton(createImageView("edit_icon.png"));
        Button deleteBtn = createButton(createImageView("delete_funny_icon.png"));

        deleteBtn.setOnAction(e -> {
            service.removeClothingItem(ci);
            showClothes();
        });

        box.getChildren().addAll(clothingView, label, editBtn, deleteBtn);
        return box;
    }

    // take the owned clothes from the wardrobe atribute of the service and display each item in it's own Vbox
    public void showClothes(){
        // clear TilePane
        clothesCard.getChildren().clear();
        for (ClothingItem ci : service.getWardrobe().getOwnedClothes()){ // iterare prin haine
            HBox card = createClothingUI(ci);
            card.setAlignment(Pos.CENTER);

            clothesCard.getChildren().add(card);
        }

    }

    @FXML
    public void initialize() {
        System.out.println("Gallery started");
        showClothes();
    }

    @FXML
    void handleAddItemButton(ActionEvent event) throws IOException {
        navigateTo(event, "add-item-view.fxml", "Add New Item");
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
