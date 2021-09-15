package com.example.solitaire;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class SolitaireController {
    @FXML
    private ToggleGroup boardType;
    @FXML
    private Label statusLabel;
    @FXML
    private GridPane board;

    @FXML
    void initialize() {
        // bind fields' sizes to grid's size
        for (Node node : board.getChildren()) {
            Rectangle rect = (Rectangle) node;
            rect.widthProperty().bind(board.widthProperty().divide(board.getColumnCount()));
            rect.heightProperty().bind(board.heightProperty().divide(board.getRowCount()));
        }
    }
}
