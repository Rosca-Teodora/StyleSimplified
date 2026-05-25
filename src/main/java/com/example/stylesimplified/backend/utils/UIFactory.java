package com.example.stylesimplified.backend.utils;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Outfit;
import com.j256.ormlite.stmt.query.In;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.io.File;

// factory design pattern for easy UI element creation
// noticed that i was trying to make a very similar layout for tags clothes and outfits and i didnt want to
// copy paste all my code all the time; the internet suggested factory :P
public class UIFactory {
    private static final String ICON_PATH = "/com/example/stylesimplified/icons/";

    private UIFactory() {}

    public static HBox createHBox(Integer width, Integer height, Insets padding) {
        HBox box = new HBox();
        box.setMinWidth(width);
        box.setPrefHeight(height);
        box.setPadding(padding);

        return box;
    }

    // default overloaded HBox creation
    public static HBox createHBox() {
        return createHBox(500, 5, new Insets(10));
    }

    public static ImageView createImageView(String icon,  String iconPath, Integer width, Integer height) {
        Image imageIcon = new Image(UIFactory.class.getResourceAsStream(iconPath + icon));
        ImageView imageView = new ImageView(imageIcon);

        imageView.setFitHeight(height);
        imageView.setFitWidth(width);

        return imageView;
    }

    // default ImageView creation
    public static ImageView createImageView(String icon) {
        return createImageView(icon, ICON_PATH, 28, 28);
    }

    public static Button createButton(Node graphic, Integer height, Integer width, String styleClass) {
        Button btn = new Button();
        btn.setPrefHeight(height);
        btn.setPrefWidth(width);
        btn.setGraphic(graphic);
        btn.getStyleClass().add(styleClass);
        return btn;
    }

    // default values pt buton
    public static Button createButton(Node graphic) {
        return createButton(graphic, 32, 8, "icon-button");
    }

    public static ImageView loadClothingImage(String path, int width, int height) {
        ImageView clothingView = new ImageView();
        try {
            File imageFile = new File(path);
            if (imageFile.exists()){
                Image img = new Image(imageFile.toURI().toString(), width, height, true, true);
                clothingView.setImage(img);
            } else {
                System.err.println("Image not found at path: " + path);
                Image placeholder = new Image(UIFactory.class.getResourceAsStream(ICON_PATH + "placeholder.jpg"), width, height, true, true);
                clothingView.setImage(placeholder);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + e.getMessage());
        }

        clothingView.setFitWidth(width);
        clothingView.setFitHeight(height);
        clothingView.setPreserveRatio(true);
        return clothingView;
    }

    public static VBox createGalleryThumbnail(ClothingItem ci) {
        ImageView imgView = loadClothingImage(ci.getImgPath(), 100, 100);

        VBox card = new VBox(imgView);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("gallery-item"); // Apply the default CSS

        return card;
    }

    public static VBox createOutfitCard(Outfit outfit){
        ImageView thumbnail = loadClothingImage(outfit.getImagePath(), 100, 100);
        Label outfitName = new Label(outfit.getName());
        outfitName.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox box = new VBox(10, thumbnail, outfitName); // spacing 10 between image and fit name
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("gallery-item");
        return box;
    }
}
