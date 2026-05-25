package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.commands.AddTagCommand;
import com.example.stylesimplified.backend.commands.CommandInvoker;
import com.example.stylesimplified.backend.models.Tag;
import com.example.stylesimplified.backend.models.Wardrobe;
import com.example.stylesimplified.backend.services.WardrobeService;
import com.example.stylesimplified.backend.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.stylesimplified.backend.utils.UIFactory.*;

public class TagsController {
    @FXML
    private TextField nameField;

    @FXML
    private VBox tagsContainer;

    private int ALL_TAGS_COUNTER = 0; // sincer doar de funsies adaugat pt ca urmaream un tutorial de task manager pt logica controller-ului asta si avea un counter

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

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Tag name is empty");
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
            HBox box = createTagUI(tag);
            tagsContainer.getChildren().add(box);
        }
    }

    HBox createTagUI(Tag tag){
        // the actual box in which everything is placed
        HBox box = createHBox();
        box.getStyleClass().add("tag");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Label style + text
        Text tagStyle = new Text(tag.getNume());
        Label label = new Label();
        label.setPrefWidth(300);
        label.setPrefHeight(25);
        label.setGraphic(tagStyle);
        tagStyle.setFill(Color.WHITE);

        // pt edit "view" -> text field nou in care se scrie si se trimite direct catre database cu service.updateTag(tag)
        TextField editField = new TextField(tag.getNume()); // initial apare numele vechi
        editField.setPrefWidth(300);
        editField.getStyleClass().add("pane");
        editField.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        editField.setVisible(false);
        editField.setManaged(false);

        // images (delete, edit)
        ImageView deleteView = createImageView("delete_funny_icon.png");
        ImageView editView = createImageView("edit_icon.png");

        // buttons (delete, edit)
        Button deleteBtn = createButton(deleteView);
        Button editBtn = createButton(editView);

        deleteBtn.setOnAction(e -> {
            service.removeTag(tag);
            renderTags();
        });

        // ok editing core logic here
        final boolean[] isEditing = {false};
        editBtn.setOnAction(e -> {
            if (!isEditing[0]){ // currently editing
                isEditing[0] = true;
                label.setVisible(false);
                label.setManaged(false);
                editField.setVisible(true);
                editField.setManaged(true);

                editField.requestFocus(); // chichita draguta = cursor in box
                editField.positionCaret(editField.getText().length());
            }
            else {
                String newName = editField.getText().trim();

                if (!newName.isEmpty() && newName.length() <= 50) {
                    tag.setNume(newName); // update the object
                    service.updateTag(tag); // save to Database
                }

                isEditing[0] = false;
                renderTags(); // Redraws the screen, turning it back into a normal Label!
            }
        });

        // hit enter and save :)
        editField.setOnAction(e -> {
            editBtn.fire(); // Simulates a click on the edit button to trigger the save logic above
        });

        String heartIconPath = tag.getFavourite() ? "filled_heart_icon.png" : "empty_heart_icon.png";
        ImageView faveView = createImageView(heartIconPath);
        Button faveBtn = createButton(faveView);

        // when clicked, flip the boolean, update the DB, and redraw to show the new heart
        faveBtn.setOnAction(e -> {
            tag.setFavourite(!tag.getFavourite());
            service.updateTag(tag);
            renderTags();
        });

        box.getChildren().addAll(label, editField, spacer, editBtn, deleteBtn, faveBtn);
        return box;
    }


    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "home-view.fxml", "Style Simplified - Home");
    }
}
