package com.example.stylesimplified.backend.controllers;

import com.example.stylesimplified.backend.models.*;
import com.example.stylesimplified.backend.services.WardrobeService;
import com.example.stylesimplified.backend.utils.ClothingNavigationContext;
import com.example.stylesimplified.backend.utils.DataInitializable;
import com.example.stylesimplified.backend.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClothingItemsController implements DataInitializable<ClothingNavigationContext> {

    private ClothingItem currentItem;
    private boolean isCurrentlyEditing = false;
    private WardrobeService service = WardrobeService.getInstance();

    @FXML private ScrollPane tagsPane;
    @FXML private FlowPane tagsFlowPane;

    @FXML private Label clothingNameLabel;
    @FXML private TextField clothingNameField;

    @FXML private Label propNameLabel1, propNameLabel2, propNameLabel3; // label-uri si text field uri generice pt ca sunt 3 tipuri diferite de iteme si tb sa le populez diferit in fct de tip
    @FXML private Label propValueLabel1, propValueLabel2, propValueLabel3;
    @FXML private TextField propField1, propField2, propField3;

    @FXML private Button editButton, backButton, saveButton, cancelButton;
    @FXML private MenuButton addTagButton;
    @FXML private ImageView clothingImageView;


    @FXML
    public void initialize() {
        clothingNameLabel.managedProperty().bind(clothingNameLabel.visibleProperty());
        clothingNameField.managedProperty().bind(clothingNameField.visibleProperty());

        Label[] dynamicLabels = {propNameLabel1, propNameLabel2, propNameLabel3, propValueLabel1, propValueLabel2, propValueLabel3};
        TextField[] dynamicFields = {propField1, propField2, propField3};

        for (Label l : dynamicLabels) l.managedProperty().bind(l.visibleProperty());
        for (TextField f : dynamicFields) f.managedProperty().bind(f.visibleProperty());

        Button[] editButtons = {saveButton, cancelButton};
        Button[] viewButtons = {editButton, backButton};

        addTagButton.managedProperty().bind(addTagButton.visibleProperty());

        for (Button b : editButtons) b.managedProperty().bind(b.visibleProperty());
        for (Button b : viewButtons) b.managedProperty().bind(b.visibleProperty());
        addTagButton.setOnAction(this::handleAddTag);
    }

    @Override
    public void initData(ClothingNavigationContext context){
        this.currentItem = context.getItem();
        this.isCurrentlyEditing = context.isStartInEditMode();

        clothingNameLabel.setText(currentItem.getName());
        clothingNameField.setText(currentItem.getName());

        File imageFile = new File(currentItem.getImgPath());
        if (imageFile.exists()){
            clothingImageView.setImage(new Image(imageFile.toURI().toString()));
        }

        renderPolymorphicAttributes(); // helper care sa pune efectiv label urile corecte in fct de type ul item-ului
        refreshTagUI(); // helpere necesare pt toate tag-urile de pe item
        setEditMode(isCurrentlyEditing);
    }

    private void renderPolymorphicAttributes() {
        if (currentItem instanceof Bottom) {
            Bottom bottom = (Bottom) currentItem;
            setupPropRow(1, "Fit Type:", bottom.getFitType(), propNameLabel1, propValueLabel1, propField1);
            setupPropRow(2, "Waist Rise:", bottom.getWaistRise(), propNameLabel2, propValueLabel2, propField2);
            setupPropRow(3, "Length:", bottom.getLength(), propNameLabel3, propValueLabel3, propField3);
        }
        else if (currentItem instanceof Top) {
            Top top = (Top) currentItem;
            setupPropRow(1, "Sleeve Length:", top.getSleeveLength(), propNameLabel1, propValueLabel1, propField1);
            setupPropRow(2, "Neckline:", top.getNeckline(), propNameLabel2, propValueLabel2, propField2);
            setupPropRow(3, "Outerwear?:", top.isOuterwear() ? "Yes" : "No", propNameLabel3, propValueLabel3, propField3);
        }
        else if (currentItem instanceof Accessory) {
            Accessory acc = (Accessory) currentItem;
            setupPropRow(1, "Placement:", acc.getPlacement(), propNameLabel1, propValueLabel1, propField1);
            setupPropRow(2, "Type", acc.getType(), propNameLabel2, propValueLabel2, propField2);
            setupPropRow(3, "Material", acc.getMaterial(), propNameLabel3, propValueLabel3, propField3);
        }
    }

    private void setupPropRow(int rowNum, String labelName, String value, Label nameL, Label valL, TextField field) {
        nameL.setVisible(true);
        nameL.setText(labelName);
        valL.setText(value != null ? value : "None");
        field.setText(value != null ? value : "");
    }

    private void hideRow(Label nameL, Label valL, TextField field) {
        nameL.setVisible(false);
        valL.setVisible(false);
        field.setVisible(false);
    }



    private void renderTagsComponent() {
        tagsFlowPane.getChildren().clear();

        for (Tag tag : currentItem.getTags()) {
            HBox badge = new HBox(8);
            badge.setAlignment(Pos.CENTER);
            badge.getStyleClass().add("tag-badge");
            badge.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));

            Label tagLabel = new Label(tag.getName());
            tagLabel.setStyle("-fx-text-fill: white;");
            badge.getChildren().add(tagLabel);

            // cand se editeaza itemul se adauga un x micut la buton... momentan nu merge 100% perfect cu menuButton ul de tag choices dar o sa il rezolv mai tarziu
            if (isCurrentlyEditing) {
                Button removeBtn = new Button("×");
                removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff6666; -fx-padding: 0; -fx-cursor: hand;");
                removeBtn.setOnAction(e -> {
                    currentItem.getTags().remove(tag);
                    WardrobeService.getInstance().removeClothingTagLink(currentItem, tag); // Clear DB junction data
                    renderTagsComponent(); // Instant view recalculation
                });
                badge.getChildren().add(removeBtn);
            }
            tagsFlowPane.getChildren().add(badge);
        }
    }

    private void populateTagMenuButton() {
        addTagButton.getItems().clear();
        List<Tag> allGlobalTags = service.getWardrobe().getTags();

        for (Tag tag : allGlobalTags) {
            CheckBox cb = new CheckBox(tag.getNume());
            cb.setStyle("-fx-text-fill: white; -fx-cursor: hand;");
            cb.setSelected(currentItem.getTags().contains(tag)); // seteaza selectatele deja in fct de array ul cached

            cb.setOnAction(e -> { // setOnAction ca sa nu fac un popup menu
                String typeStr = "accessory";
                if (currentItem instanceof Top) typeStr = "top";
                else if (currentItem instanceof Bottom) typeStr = "bottom";

                if (cb.isSelected()) {
                    if (!currentItem.getTags().contains(tag)) {
                        currentItem.getTags().add(tag);
                        ClothingTagLink newLink = new ClothingTagLink(tag, currentItem.getItemId(), typeStr);
                        service.addClothingTagLink(newLink);
                    }
                } else {
                    currentItem.getTags().remove(tag);
                    service.removeClothingTagLink(currentItem, tag);
                }
                renderTagsComponent(); // super important update ca sa dea refresh la tag uri fara sa inchizi meniul
            });

            CustomMenuItem menuItem = new CustomMenuItem(cb);
            menuItem.setHideOnClick(false); // tine meniul open pt mai multe click uri
            addTagButton.getItems().add(menuItem);
        }
    }

    private void refreshTagUI() {
        renderTagsComponent();
        populateTagMenuButton();
    }


    // editMode e boolean
    // !editMode cu field urile normale
    // editMode cu field urile de introdus text TextField
    private void setEditMode(boolean editMode){
        this.isCurrentlyEditing = editMode;

        clothingNameField.setVisible(editMode);
        propField1.setVisible(editMode && propNameLabel1.isVisible());
        propField2.setVisible(editMode && propNameLabel2.isVisible());
        propField3.setVisible(editMode && propNameLabel3.isVisible());

        saveButton.setVisible(editMode);
        cancelButton.setVisible(editMode);
        addTagButton.setVisible(editMode);

        clothingNameLabel.setVisible(!editMode);
        propValueLabel1.setVisible(!editMode && propNameLabel1.isVisible());
        propValueLabel2.setVisible(!editMode && propNameLabel2.isVisible());
        propValueLabel3.setVisible(!editMode && propNameLabel3.isVisible());

        editButton.setVisible(!editMode);
        backButton.setVisible(!editMode);

        renderTagsComponent(); // refresh la final
    }

    @FXML void handleEditMode(ActionEvent event) { setEditMode(true); }
    @FXML void handleCancel(ActionEvent event) { setEditMode(false); }

    private void handleAddTag(ActionEvent event) {
        List<Tag> allGlobalTags = service.getWardrobe().getTags();

        // filtreaza optiunile tag urilor ca sa nu ia direct ceea ce era deja selectat
        List<Tag> choices = allGlobalTags.stream()
                .filter(t -> !currentItem.getTags().contains(t))
                .toList();

        if (choices.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No new tags available to add! Create more in tags view.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        List<String> tagNames = choices.stream().map(Tag::getNume).toList(); // mapare ca sa iau doar numele

        ChoiceDialog<String> dialog = new ChoiceDialog<>(tagNames.get(0), tagNames);
        dialog.setTitle("Attach Tag");
        dialog.setHeaderText("Choose a tag to describe this item:");
        dialog.setContentText("Tag name:");

        dialog.showAndWait().ifPresent(selectedName -> {
            Tag matchedTag = choices.stream() // gasesc tag in fct de nume
                    .filter(t -> t.getNume().equals(selectedName))
                    .findFirst()
                    .orElse(null);

            if (matchedTag != null) {
                currentItem.getTags().add(matchedTag);
                String typeStr = "accessory";
                if (currentItem instanceof Top) typeStr = "top";
                else if (currentItem instanceof Bottom) typeStr = "bottom";

                ClothingTagLink newLink = new ClothingTagLink(matchedTag, currentItem.getItemId(), typeStr); // salvarea tag-ului (relatiei) in DB
                service.addClothingTagLink(newLink);
                renderTagsComponent(); // refresh ul
            }
        });
    }

    @FXML
    void handleSave(ActionEvent event) {
        currentItem.setName(clothingNameField.getText());
        clothingNameLabel.setText(currentItem.getName());

        if (currentItem instanceof Bottom) {
            Bottom b = (Bottom) currentItem;
            b.setFitType(propField1.getText());
            b.setWaistRise(propField2.getText());
            b.setLength(propField3.getText());
        } else if (currentItem instanceof Top) {
            Top t = (Top) currentItem;
            t.setSleeveLength(propField1.getText());
            t.setNeckline(propField2.getText());
            // TODO:  checkbox !!!!!11!!1
        } else if (currentItem instanceof Accessory) {
            Accessory a = (Accessory) currentItem;
            a.setType(propField2.getText());
            a.setMaterial(propField3.getText());
            a.setPlacement(propField1.getText());
        }

        service.updateClothingItem(currentItem);

        setEditMode(false);
    }

    @FXML
    void handleBack(ActionEvent event) throws IOException {
        SceneManager.navigateTo(event, "wardrobe-view.fxml", "My Wardrobe");
    }
}