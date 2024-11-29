package com.app.controllers;

import com.app.db.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
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

    @FXML
    private void handleInsertProduct() {
        String nom = nomField.getText();
        String marque = marqueField.getText();
        String prix = prixField.getText();
        String categorie = categorieComboBox.getValue();

        if (nom.isEmpty() || marque.isEmpty() || prix.isEmpty() || categorie == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO produit (nom, marque, prix, categorie) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nom);
            statement.setString(2, marque);
            statement.setDouble(3, Double.parseDouble(prix));
            statement.setString(4, categorie);

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

    public void redirectToHome(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the Home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml")); // Update path as needed
            VBox root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'accueil !");
        }
    }
}
