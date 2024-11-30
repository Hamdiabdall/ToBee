package com.app.controllers;

import com.app.db.DatabaseConnection;
import com.app.models.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateProductController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField marqueField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField categorieField;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;

        // Populate fields with the product's data
        nomField.setText(product.getNom());
        marqueField.setText(product.getMarque());
        prixField.setText(String.valueOf(product.getPrix()));
        categorieField.setText(product.getCategorie());
    }

    @FXML
    private void handleUpdate() {
        String nom = nomField.getText();
        String marque = marqueField.getText();
        String prix = prixField.getText();
        String categorie = categorieField.getText();

        if (nom.isEmpty() || marque.isEmpty() || prix.isEmpty() || categorie.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE produit SET nom = ?, marque = ?, prix = ?, categorie = ? WHERE matricule = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nom);
            statement.setString(2, marque);
            statement.setDouble(3, Double.parseDouble(prix));
            statement.setString(4, categorie);
            statement.setInt(5, product.getMatricule());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Succès", "Produit mis à jour avec succès !");
                redirectToHome();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour !");
        }
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        handleUpdate();
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        redirectToHome();
    }

    private void redirectToHome() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de retourner à la page d'accueil !");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
