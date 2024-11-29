package com.app.models;

import javafx.beans.property.*;

public class Product {

    private final IntegerProperty matricule;
    private final StringProperty nom;
    private final StringProperty marque;
    private final DoubleProperty prix;
    private final StringProperty categorie;

    // Constructeur
    public Product(int matricule, String nom, String marque, double prix, String categorie) {
        this.matricule = new SimpleIntegerProperty(matricule);
        this.nom = new SimpleStringProperty(nom);
        this.marque = new SimpleStringProperty(marque);
        this.prix = new SimpleDoubleProperty(prix);
        this.categorie = new SimpleStringProperty(categorie);
    }

    // Getters et setters
    public int getMatricule() {
        return matricule.get();
    }

    public void setMatricule(int matricule) {
        this.matricule.set(matricule);
    }

    public IntegerProperty matriculeProperty() {
        return matricule;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getMarque() {
        return marque.get();
    }

    public void setMarque(String marque) {
        this.marque.set(marque);
    }

    public StringProperty marqueProperty() {
        return marque;
    }

    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public DoubleProperty prixProperty() {
        return prix;
    }

    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }

    public StringProperty categorieProperty() {
        return categorie;
    }
}
