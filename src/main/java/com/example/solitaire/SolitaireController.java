package com.example.solitaire;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SolitaireController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}