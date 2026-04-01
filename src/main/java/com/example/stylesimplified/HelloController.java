package com.example.stylesimplified;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onAddOutfitButtonClick() {
        welcomeText.setText("Button pressed!");
    }
}
