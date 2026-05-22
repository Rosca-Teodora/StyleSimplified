package com.example.stylesimplified.backend.utils;

import com.j256.ormlite.stmt.query.In;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

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
}
