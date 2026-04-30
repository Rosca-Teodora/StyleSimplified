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
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

public class WardrobeController {

    private WardrobeService service = WardrobeService.getInstance();

    // TilePane from current scene (list of clothes)
    @FXML
    private TilePane clothesCard;

    // take the owned clothes from the wardrobe atribute of the service and display each item in it's own Vbox
    public void showClothes(){
        // clear TilePane
        clothesCard.getChildren().clear();
        for (ClothingItem ci : service.getWardrobe().getOwnedClothes()){ // iterare prin haine
            // creare VBox
            VBox card = new VBox();
            card.setAlignment(Pos.CENTER);

            // fiecare item are o poza deci tb creat un ImageView
            ImageView img = new ImageView();
            Label label = new Label(ci.getName()); // label-ul imaginii

            File imageFile = new File(ci.getImgPath());
            if (imageFile.exists()){
                img.setImage(new javafx.scene.image.Image(imageFile.toURI().toString()));
            }
            else {
                System.out.println("Image not found for" + ci.getName());
            }
            // formatare necesara pt imagini (altfel ImageView le afiseaza la marimea lor default si acopera TOATA pagina)
            img.setFitWidth(200);
            img.setFitHeight(200);
            img.setPreserveRatio(true);


            card.getChildren().addAll(img, label);
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