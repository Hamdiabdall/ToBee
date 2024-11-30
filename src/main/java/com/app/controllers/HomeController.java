package com.app.controllers;

import com.app.db.DatabaseConnection;
import com.app.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HomeController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> matriculeColumn;
    @FXML
    private TableColumn<Product, String> nomColumn;
    @FXML
    private TableColumn<Product, String> marqueColumn;
    @FXML
    private TableColumn<Product, Double> prixColumn;
    @FXML
    private TableColumn<Product, String> categorieColumn;
    @FXML
    private ComboBox<String> categoryComboBox;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        matriculeColumn.setCellValueFactory(cellData -> cellData.getValue().matriculeProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        marqueColumn.setCellValueFactory(cellData -> cellData.getValue().marqueProperty());
        prixColumn.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
        categorieColumn.setCellValueFactory(cellData -> cellData.getValue().categorieProperty());

        loadProducts("informatique");

        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadProducts(newValue);
            }
        });
    }

    private void loadProducts(String category) {
        productList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM produit WHERE categorie = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                productList.add(new Product(
                        resultSet.getInt("matricule"),
                        resultSet.getString("nom"),
                        resultSet.getString("marque"),
                        resultSet.getDouble("prix"),
                        resultSet.getString("categorie")
                ));
            }

            productTable.setItems(productList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void redirectToInsertProduct() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/insert_produit.fxml"));
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCategoryChange(ActionEvent actionEvent) {
        // Récupérer la catégorie sélectionnée dans le ComboBox
        String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        // Vérifier si une catégorie est sélectionnée
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            // Charger les produits correspondant à la catégorie sélectionnée
            loadProducts(selectedCategory);
        } else {
            // Si aucune catégorie n'est sélectionnée, vider le tableau
            productList.clear();
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM produit WHERE matricule = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, selectedProduct.getMatricule());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    productList.remove(selectedProduct);
                    showAlert("Succès", "Produit supprimé avec succès !");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression !");
            }
        } else {
            showAlert("Alerte", "Veuillez sélectionner un produit à supprimer !");
        }
    }




    @FXML
    private void handleUpdateProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/update_product.fxml"));
                Stage stage = (Stage) productTable.getScene().getWindow();
                stage.setScene(new Scene(fxmlLoader.load()));

                // Pass the selected product to the UpdateProductController
                UpdateProductController controller = fxmlLoader.getController();
                controller.setProduct(selectedProduct);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la page de mise à jour !");
            }
        } else {
            showAlert("Alerte", "Veuillez sélectionner un produit à modifier !");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
