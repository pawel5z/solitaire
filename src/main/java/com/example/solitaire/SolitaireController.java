package com.example.solitaire;

import com.example.solitaire.backend.Solitaire;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class SolitaireController {
    private Solitaire solitaire;
    private Circle markedPeg = null;

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

        setPegs();
    }

    @FXML
    void fieldClicked(MouseEvent event) {
        event.consume();
        if (markedPeg == null) {
            return;
        }
        Rectangle clickedField = (Rectangle) event.getTarget();
        Pair<Integer, Integer> posToRemove = solitaire.move(GridPane.getRowIndex(markedPeg),
                                                            GridPane.getColumnIndex(markedPeg),
                                                            GridPane.getRowIndex(clickedField),
                                                            GridPane.getColumnIndex(clickedField));
        if (posToRemove == null)
            return;
        board.getChildren().remove(markedPeg);
        board.add(markedPeg, GridPane.getColumnIndex(clickedField), GridPane.getRowIndex(clickedField));
        board.getChildren().remove(getPegByRowCol(posToRemove.getKey(), posToRemove.getValue()));
    }

    private void setPegs() {
        // remove any remaining pegs
        for (var peg : board.getChildren()) {
            if (peg instanceof Circle)
                board.getChildren().remove(peg);
        }

        // set up new pegs
        solitaire = new Solitaire();
        for (Pair<Integer, Integer> pPos : solitaire.getPegPositions()) {
            Circle p = createPeg();
            board.add(p, pPos.getValue(), pPos.getKey());
        }
    }

    private Circle createPeg() {
        Circle peg = new Circle();
        peg.radiusProperty().bind(Bindings.min(board.widthProperty().divide(board.getColumnCount()),
                board.heightProperty().divide(board.getRowCount())).divide(2).multiply(0.9));
        peg.setFill(Paint.valueOf("0x000000"));

        peg.setOnMouseClicked(mouseEvent -> {
            mouseEvent.consume();
            markedPeg = (Circle) mouseEvent.getTarget();
        });

        return peg;
    }

    private Circle getPegByRowCol(int r, int c) {
        for (var peg : board.getChildren()) {
            if (peg instanceof Circle && GridPane.getRowIndex(peg) == r && GridPane.getColumnIndex(peg) == c)
                return (Circle) peg;
        }
        return null;
    }
}
