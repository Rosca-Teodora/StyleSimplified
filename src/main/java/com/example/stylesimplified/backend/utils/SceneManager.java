package com.example.stylesimplified.backend.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

// class made to stop copy pasting the navigateTo method in all my controllers...
// utility design pattern
public final class SceneManager {

    private SceneManager() {}

    public static void navigateTo(ActionEvent event, String fxmlFile, String title) throws IOException {
        String absoluteFxmlPath = "/com/example/stylesimplified/" + fxmlFile;
        Parent root = FXMLLoader.load(SceneManager.class.getResource(absoluteFxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    // needed method to stop reusing code when entering the view/edit/ details mode for objects (data needs to be sent from controller care implementeaza interfata DataInitializable)
    public static <T> void navigateToWithData(Node sourceNode, String fxmlFile, String title, T data) {
        try {
            var resource = SceneManager.class.getResource("/com/example/stylesimplified/" + fxmlFile);

//            if (resource == null) {
//                resource = SceneManager.class.getResource("/com.example.stylesimplified/" + fxmlFile);
//            }

            if (resource == null) {
                throw new IOException("JavaFX cannot find " + fxmlFile + " in your resources folder.");
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Object controller = loader.getController(); // load controller si trimite propriu-zis data to controller
            if (controller instanceof DataInitializable) {
                @SuppressWarnings("unchecked") // imi aparea mesajul //unchecked// si l-am apasat si mi-a dat intelliJ ul asta? presupun ca e vorba de tipul de date
                DataInitializable<T> dataController = (DataInitializable<T>) controller;
                dataController.initData(data); // callback-ul doar atunci cand implementeaza interfata pt el
            }

            Stage stage = (Stage) sourceNode.getScene().getWindow(); // scene switch propriu-zis
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            System.err.println("Navigation Failed! Check your FXML folder path structure.");
            e.printStackTrace();
        }
    }
}