package com.example.stylesimplified;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField imagePathField;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private Label statusLabel;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeChoiceBox.getItems().addAll("Top", "Bottom", "Accessory");
        typeChoiceBox.setValue("Top"); // Default value
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
    void handleAddItemButton(ActionEvent event) {
        String name = nameField.getText();
        String imagePath = imagePathField.getText();
        String type = typeChoiceBox.getValue();

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
                    newClothingItem = new Top(name, imagePath, sleeveLength, neckline, isOuterwear);
                    break;
                case "Bottom":
                    String fitType = fitTypeInput.getText();
                    String waistRise = waistRiseInput.getText();
                    String length = lengthInput.getText();
                    newClothingItem = new Bottom(name, imagePath, fitType, waistRise, length);
                    break;
                case "Accessory":
                    String placement = placementInput.getText();
                    String material = materialInput.getText();
                    String accessoryType = accessoryTypeInput.getText();
                    newClothingItem = new Accessory(name, imagePath, placement, material, accessoryType);
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
        imagePathField.clear();
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
