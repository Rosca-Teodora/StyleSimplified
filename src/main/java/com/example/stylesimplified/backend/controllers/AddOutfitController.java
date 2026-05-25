package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.ClothingTagLink;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddOutfitController {

    @FXML
    private ImageView thumbnailPreview;

    @FXML
    private FlowPane clothesGallery;

    @FXML
    private Label counterLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField outfitNameField;

    private final List<ClothingItem> selectedClothes = new ArrayList<>(); // clicked clothes
    private File selectedImageFile;

    private WardrobeService service = WardrobeService.getInstance();

    @FXML
    public void initialize() {
        thumbnailPreview.setVisible(false);
        populateGalery();
    }

    private void populateGalery(){
        clothesGallery.getChildren().clear(); // reset
        Set<ClothingItem> allClothes = service.getWardrobe().getOwnedClothes();

        for (ClothingItem ci : allClothes) {
            VBox clothingcard = UIFactory.createGalleryThumbnail(ci);
            clothingcard.setOnMouseClicked(e -> handleItemClick(clothingcard, ci)); // lambda on event you want to select the clothing item
            clothesGallery.getChildren().add(clothingcard);
        }
    }

    // item click = selecting/ deselecting an item
    private void handleItemClick(VBox clothingCard, ClothingItem ci) {
        // select the ci into the outfit = add to local cache, change style for UI
        if (selectedClothes.contains(ci)){ // if it was already selected it means user wanted to remove from list
            selectedClothes.remove(ci);
            clothingCard.getStyleClass().remove("gallery-item-selected");
            clothingCard.getStyleClass().add("gallery-item");
        }
        else { // select item
            if (selectedClothes.size() < 30) { // max selection caps at 30 (absurd amount of clothes to put in an item anyway but like just saying)
                 selectedClothes.add(ci);
                 clothingCard.getStyleClass().remove("gallery-item");
                 clothingCard.getStyleClass().add("gallery-item-selected");
            }
        }
        counterLabel.setText(selectedClothes.size() + " / 30 selected");
    }

    @FXML
    private void handleCustomThumbnail(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select outfit thumbnail");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            Image preview = new Image(selectedImageFile.toURI().toString());
            thumbnailPreview.setImage(preview);
            thumbnailPreview.setVisible(true);
            thumbnailPreview.setManaged(true);
        }
    }

    @FXML
    private void handleSaveOutfitButton(ActionEvent event) throws IOException {
        String name = outfitNameField.getText();
        String thumbnailPath = null;

        if (name.isEmpty()) {
            statusLabel.setText("Enter an outfit name");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (selectedClothes.isEmpty()) {
            statusLabel.setText("Outfit must have at least one clothing item");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (selectedImageFile != null) {
            try {
                String fileName = System.currentTimeMillis() + "-" + selectedImageFile.getName();
                Path destinationDirectory = Paths.get("outfit_thumbnails");
                if (!Files.exists(destinationDirectory)){
                    Files.createDirectories(destinationDirectory);
                }

                Path destinationPath = destinationDirectory.resolve(fileName);
                Files.copy(selectedImageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                thumbnailPath = destinationPath.toString();
            }
            catch (Exception e) {
                System.out.println("Couldnt create directory or copy thumbnail image");
                e.printStackTrace();
            }
        }

        if (thumbnailPath == null) {
            thumbnailPath = selectedClothes.get(0).getImgPath();
        }

        Outfit newOutfit = new Outfit(name, thumbnailPath);
        service.createOutfit(newOutfit, selectedClothes);

        // after saving outfit simply return to outfit list page
        SceneManager.navigateTo(event, "outfits-view.fxml", "My outfits");
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "outfits-view.fxml", "My Outfits");
    }

}
