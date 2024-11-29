package com.app.controllers;

import com.app.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertProductController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField marqueField;
    @FXML
    private TextField prixField;
    @FXML
    private ComboBox<String> categorieComboBox;

    private String imagePath;

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
        }
    }

    @FXML
    private void handleInsertProduct() {
        String nom = nomField.getText();
        String marque = marqueField.getText();
        String prix = prixField.getText();
        String categorie = categorieComboBox.getValue();

        if (nom.isEmpty() || marque.isEmpty() || prix.isEmpty() || categorie == null || imagePath == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO produit (nom, marque, prix, categorie, image) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nom);
            statement.setString(2, marque);
            statement.setDouble(3, Double.parseDouble(prix));
            statement.setString(4, categorie);
            statement.setString(5, imagePath);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Succès", "Produit ajouté avec succès !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
