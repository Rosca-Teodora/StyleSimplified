package com.example.stylesimplified;

import com.example.stylesimplified.backend.commands.CommandInvoker;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField imagePathField;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private Label statusLabel;

    private final CommandInvoker cmdInvoker = new CommandInvoker();

    // initializeaza choice box-ul rn (cu valorile care pot fi selectate)
    @FXML
    public void initialize() {
        typeChoiceBox.getItems().addAll("Top", "Bottom", "Accessory");
        typeChoiceBox.setValue("Top"); // valoare implicita
    }


}
