package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.commands.AddTagCommand;
import com.example.stylesimplified.backend.commands.CommandInvoker;
import com.example.stylesimplified.backend.models.Tag;
import com.example.stylesimplified.backend.models.Wardrobe;
import com.example.stylesimplified.backend.services.WardrobeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagsController {
    @FXML
    private TextField nameField;

    @FXML
    private VBox tagsContainer;

    private final WardrobeService service = WardrobeService.getInstance();
    private final CommandInvoker commandInvoker = new CommandInvoker();

    @FXML
    public void initialize() {
        tagsContainer.setAlignment(Pos.TOP_CENTER);
        renderTags();
    }

    @FXML
    void handleAddTagButton(ActionEvent event) throws IOException {
        String name = nameField.getText();

        if (name.isEmpty()) {;
            return;
        }

        Tag newTag = new Tag(name);
        AddTagCommand addTagCommand = new AddTagCommand(service, newTag);
        commandInvoker.executeCommand(addTagCommand);

        nameField.clear();
        renderTags(); // redraw after adding a tag
    }

    @FXML
    void renderTags() {
        tagsContainer.getChildren().clear();

        List<Tag> allTags = new ArrayList<>();
        allTags = service.getWardrobe().getTags();

        for (Tag tag : allTags) {

        }
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
