package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.commands.AddOutfitCommand;
import com.example.stylesimplified.backend.commands.Command;
import com.example.stylesimplified.backend.commands.CommandInvoker;
import com.example.stylesimplified.backend.commands.UpdateOutfitCommand;
import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Outfit;
import com.example.stylesimplified.backend.services.WardrobeService;
import com.example.stylesimplified.backend.utils.DataInitializable;
import com.example.stylesimplified.backend.utils.OutfitNavigationContext;
import com.example.stylesimplified.backend.utils.SceneManager;
import com.example.stylesimplified.backend.utils.UIFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

public class AddOutfitController implements DataInitializable<OutfitNavigationContext> {

    @FXML private ImageView thumbnailPreview;
    @FXML private FlowPane clothesGallery;
    @FXML private Label counterLabel, statusLabel, headerLabel;
    @FXML private TextField outfitNameField;
    @FXML private Button customThumbButton;

    private final List<ClothingItem> selectedClothes = new ArrayList<>();
    private File selectedImageFile;
    private WardrobeService service = WardrobeService.getInstance();
    private final CommandInvoker cmdInvoker = new CommandInvoker();

    private Outfit outfitToEdit = null; // outfit-ul care e editat curent

    @FXML
    public void initialize() {
        thumbnailPreview.setVisible(false);
    }

    @Override
    public void initData(OutfitNavigationContext context) {
        if (context != null && context.getOutfit() != null) {
            // cazul in care chiar au fost transmise date de la controller ul anterior care a facut scene change ul ceea ce duce la edit mode
            this.outfitToEdit = context.getOutfit();
            this.selectedClothes.addAll(outfitToEdit.getClothes());
            outfitNameField.setText(outfitToEdit.getName());
            outfitNameField.setDisable(false);

            customThumbButton.setVisible(true); // se poate schimba in continuare thumbnailul

            thumbnailPreview.setImage(new Image(new File(outfitToEdit.getImagePath()).toURI().toString())); // preview buton curent
            thumbnailPreview.setVisible(true);
            thumbnailPreview.setManaged(true);

            if (headerLabel != null) headerLabel.setText("Edit Outfit: " + outfitToEdit.getName());
        } else {
            //  simplu create mode
            outfitNameField.setDisable(false);
            customThumbButton.setVisible(true);
            if (headerLabel != null) headerLabel.setText("Create New Outfit");
        }

        populateGallery();
        updateCounter();
    }

    private void populateGallery() {
        clothesGallery.getChildren().clear();
        Set<ClothingItem> allClothes = service.getWardrobe().getOwnedClothes();

        for (ClothingItem ci : allClothes) {
            VBox clothingcard = UIFactory.createGalleryThumbnail(ci);

            // daca e deja in edit mode apar selectate hainele
            if (selectedClothes.contains(ci)) {
                clothingcard.getStyleClass().remove("gallery-item");
                clothingcard.getStyleClass().add("gallery-item-selected");
            }

            clothingcard.setOnMouseClicked(e -> handleItemClick(clothingcard, ci));
            clothesGallery.getChildren().add(clothingcard);
        }
    }

    private void handleItemClick(VBox clothingCard, ClothingItem ci) {
        if (selectedClothes.contains(ci)){
            selectedClothes.remove(ci);
            clothingCard.getStyleClass().remove("gallery-item-selected");
            clothingCard.getStyleClass().add("gallery-item");
        }
        else {
            if (selectedClothes.size() < 30) {
                selectedClothes.add(ci);
                clothingCard.getStyleClass().remove("gallery-item");
                clothingCard.getStyleClass().add("gallery-item-selected");
            }
        }
        updateCounter();
    }

    private void updateCounter() {
        counterLabel.setText(selectedClothes.size() + " / 30 selected");
    }

    @FXML
    private void handleCustomThumbnail(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select outfit thumbnail");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            thumbnailPreview.setImage(new Image(selectedImageFile.toURI().toString()));
            thumbnailPreview.setVisible(true);
            thumbnailPreview.setManaged(true);
        }
    }

    @FXML
    private void handleSaveOutfitButton(ActionEvent event) throws IOException {
        // salvare pt edit mode
        if (outfitToEdit != null) {
            if (outfitNameField.getText().isEmpty() || selectedClothes.isEmpty()) {
                statusLabel.setText(selectedClothes.isEmpty() ? "Outfit must have at least one item" : "Enter an outfit name");
                statusLabel.setTextFill(Color.RED);
                return;
            }
            outfitToEdit.setName(outfitNameField.getText());
            if (selectedImageFile != null) { // in cazul unui upload nou de thumbnail
                try {
                    String fileName = System.currentTimeMillis() + "-" + selectedImageFile.getName();
                    Path destinationDirectory = Paths.get("outfit_thumbnails");
                    if (!Files.exists(destinationDirectory)) Files.createDirectories(destinationDirectory);
                    Path destinationPath = destinationDirectory.resolve(fileName);
                    Files.copy(selectedImageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                    outfitToEdit.setCustomThumbnailPath(destinationPath.toString());
                } catch (Exception e) { e.printStackTrace(); }
            } else if (outfitToEdit.getImagePath().equals("wardrobe_images/default.png") && !selectedClothes.isEmpty()) {
                // fallback pt primul item ca thumbnail
                outfitToEdit.setCustomThumbnailPath(selectedClothes.get(0).getImgPath());
            }

            Command updateOutfit = new UpdateOutfitCommand(service, outfitToEdit, selectedClothes);
            cmdInvoker.executeCommand(updateOutfit);


            OutfitNavigationContext context = new OutfitNavigationContext(outfitToEdit, false);
            SceneManager.navigateToWithData((Node) event.getSource(), "outfit-details-view.fxml", outfitToEdit.getName(), context);
            return;
        }

        // save flow ul pt create simplu
        String name = outfitNameField.getText();
        String thumbnailPath = null;

        if (name.isEmpty() || selectedClothes.isEmpty()) {
            statusLabel.setText(name.isEmpty() ? "Enter an outfit name" : "Select at least one item");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        if (selectedImageFile != null) {
            try {
                String fileName = System.currentTimeMillis() + "-" + selectedImageFile.getName();
                Path destinationDirectory = Paths.get("outfit_thumbnails");
                if (!Files.exists(destinationDirectory)) Files.createDirectories(destinationDirectory);
                Path destinationPath = destinationDirectory.resolve(fileName);
                Files.copy(selectedImageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                thumbnailPath = destinationPath.toString();
            } catch (Exception e) { e.printStackTrace(); }
        }

        if (thumbnailPath == null) thumbnailPath = selectedClothes.get(0).getImgPath();

        Outfit newOutfit = new Outfit(name, thumbnailPath);
        cmdInvoker.executeCommand(new AddOutfitCommand(service, newOutfit, selectedClothes));

        SceneManager.navigateTo(event, "outfits-view.fxml", "My outfits");
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        if (outfitToEdit != null) {
            // daca editam go back to details page
            OutfitNavigationContext context = new OutfitNavigationContext(outfitToEdit, true);
            SceneManager.navigateToWithData((Node) event.getSource(), "outfit-details-view.fxml", outfitToEdit.getName(), context);
        } else {
            SceneManager.navigateTo(event, "outfits-view.fxml", "My Outfits");
        }
    }
}