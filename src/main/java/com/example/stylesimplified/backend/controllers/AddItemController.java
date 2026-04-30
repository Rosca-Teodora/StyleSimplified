package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.commands.AddClothingCommand;
import com.example.stylesimplified.backend.commands.Command;
import com.example.stylesimplified.backend.commands.CommandInvoker;
import com.example.stylesimplified.backend.models.Accessory;
import com.example.stylesimplified.backend.models.Bottom;
import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Top;
import com.example.stylesimplified.backend.services.WardrobeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class AddItemController {
    // general clothing item atributes
    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private Label statusLabel;
    // id of the box in which the image will be previewed
    @FXML private ImageView imagePreview;
    private File selectedImageFile = null; // remembers file chosen

    // Top specific fields
    @FXML
    private AnchorPane topInputsContainer;
    @FXML
    private TextField sleeveInput;
    @FXML
    private TextField necklineInput;
    @FXML
    private ToggleButton isOuterwearInput;

    // Bottom specific fields
    @FXML
    private AnchorPane bottomInputsContainer;
    @FXML
    private TextField fitTypeInput;
    @FXML
    private TextField waistRiseInput;
    @FXML
    private TextField lengthInput;

    // Accessory specific fields
    @FXML
    private AnchorPane accessoryInputsContainer;
    @FXML
    private TextField placementInput;
    @FXML
    private TextField materialInput;
    @FXML
    private TextField accessoryTypeInput;


    private final CommandInvoker cmdInvoker = new CommandInvoker();

    @FXML
    public void initialize() {
        typeChoiceBox.getItems().addAll("Top", "Bottom", "Accessory");
        typeChoiceBox.setValue("Top");
        updateVisibleFields();

        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateVisibleFields();
        });
    }

    private void updateVisibleFields() {
        String selectedType = typeChoiceBox.getValue();
        topInputsContainer.setVisible("Top".equals(selectedType));
        topInputsContainer.setManaged("Top".equals(selectedType));
        bottomInputsContainer.setVisible("Bottom".equals(selectedType));
        bottomInputsContainer.setManaged("Bottom".equals(selectedType));
        accessoryInputsContainer.setVisible("Accessory".equals(selectedType));
        accessoryInputsContainer.setManaged("Accessory".equals(selectedType));
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        // Make sure "wardrobe-view.fxml" is the exact name of your file!
        navigateTo(event, "wardrobe-view.fxml", "My Wardrobe");
    }

    private void navigateTo(ActionEvent event, String fxmlFile, String title) throws IOException {
        String absoluteFxmlPath = "/com/example/stylesimplified/" + fxmlFile; // Check this folder path!
        Parent root = FXMLLoader.load(getClass().getResource(absoluteFxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    @FXML
    public void handleSelectImageButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Clothing Photo");

        // Only allow image files
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Get the current window (Stage) from the event source
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        // Open the window and get the file
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            // Render the preview immediately on the form!
            Image preview = new Image(selectedImageFile.toURI().toString());
            imagePreview.setImage(preview);
        }
    }

    @FXML
    void handleAddItemButton(ActionEvent event) {
        String name = nameField.getText();
        String type = typeChoiceBox.getValue();
        String finalImagePath = "wardrobe_images/default.png"; // Fallback if no photo selected

        if (selectedImageFile != null) {
            try {
                // 1. Define where you want to copy the image to
                String fileName = System.currentTimeMillis() + "_" + selectedImageFile.getName(); // Make name unique
                Path destinationDir = Paths.get("wardrobe_images");

                // Create the folder if it doesn't exist
                if (!Files.exists(destinationDir)) {
                    Files.createDirectories(destinationDir);
                }

                Path destinationPath = destinationDir.resolve(fileName);

                // 2. Actually copy the file on the hard drive
                Files.copy(selectedImageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // 3. Save the relative text path
                finalImagePath = destinationPath.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (name.isEmpty()) {
            statusLabel.setText("Item must have a name!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        ClothingItem newClothingItem = null;
        try {
            switch (type) {
                case "Top":
                    String sleeveLength = sleeveInput.getText();
                    String neckline = necklineInput.getText();
                    boolean isOuterwear = isOuterwearInput.isSelected();
                    newClothingItem = new Top(name, finalImagePath, sleeveLength, neckline, isOuterwear);
                    break;
                case "Bottom":
                    String fitType = fitTypeInput.getText();
                    String waistRise = waistRiseInput.getText();
                    String length = lengthInput.getText();
                    newClothingItem = new Bottom(name, finalImagePath, fitType, waistRise, length);
                    break;
                case "Accessory":
                    String placement = placementInput.getText();
                    String material = materialInput.getText();
                    String accessoryType = accessoryTypeInput.getText();
                    newClothingItem = new Accessory(name, finalImagePath, placement, material, accessoryType);
                    break;
            }

            if (newClothingItem != null) {
                Command addCommand = new AddClothingCommand(WardrobeService.getInstance(), newClothingItem);
                cmdInvoker.executeCommand(addCommand);
                statusLabel.setText("Item added successfully!");
                statusLabel.setTextFill(Color.GREEN);
                clearFields();
            }
        } catch (Exception e) {
            statusLabel.setText("Error creating item: " + e.getMessage());
            statusLabel.setTextFill(Color.RED);
        }
    }

    private void clearFields() {
        nameField.clear();
        selectedImageFile = null;
        imagePreview.setImage(null);
        sleeveInput.clear();
        necklineInput.clear();
        isOuterwearInput.setSelected(false);
        fitTypeInput.clear();
        waistRiseInput.clear();
        lengthInput.clear();
        placementInput.clear();
        materialInput.clear();
        accessoryTypeInput.clear();
    }
}
