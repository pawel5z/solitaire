package com.example.solitaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InformativeModalWindowController {
    @FXML
    void okButtonClicked(ActionEvent event) {
        ((Stage) (((Button) event.getTarget()).getScene().getWindow())).close();
    }
}
