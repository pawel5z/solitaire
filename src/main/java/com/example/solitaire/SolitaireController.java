package com.example.solitaire;

import com.example.solitaire.backend.Solitaire;
import com.example.solitaire.backend.SolitaireBoardType;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SolitaireController {
    private Solitaire solitaire;
    private Circle markedPeg = null;

    @FXML
    private ToggleGroup boardTypeToggleGroup;
    @FXML
    private RadioMenuItem britishBoardRadio;
    @FXML
    private RadioMenuItem europeanBoardRadio;
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

        setPegs(SolitaireBoardType.BRITISH);
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


        setDisableBoardTypeRadios(!solitaire.isGameOver());

        updateStatusLabel();
    }

    private void setPegs(SolitaireBoardType solitaireBoardType) {
        // remove any remaining pegs
        ArrayList<Node> pegsToRemove = new ArrayList<>();
        for (var peg : board.getChildren())
            if (peg instanceof Circle)
                pegsToRemove.add(peg);
        board.getChildren().removeAll(pegsToRemove);

        solitaire = new Solitaire(solitaireBoardType);

        // update fields according to game variant
        for (var field : board.getChildren()) {
            int r = GridPane.getRowIndex(field);
            int c = GridPane.getColumnIndex(field);
            field.setVisible(solitaire.inBoard(r, c));
        }

        // set up new pegs
        for (Pair<Integer, Integer> pPos : solitaire.getPegPositions()) {
            Circle p = createPeg();
            board.add(p, pPos.getValue(), pPos.getKey());
        }

        updateStatusLabel();
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

    private void updateStatusLabel() {
        String newText;
        if (solitaire.isWin())
            newText = "You win!";
        else if (solitaire.isLose())
            newText = "Game over. " + solitaire.pegsLeftCount() + " pegs left.";
        else
            newText = "Game in progress. " + solitaire.pegsLeftCount() + " pegs left.";
        statusLabel.setText(newText);
    }

    private void setDisableBoardTypeRadios(boolean b) {
        britishBoardRadio.setDisable(b);
        europeanBoardRadio.setDisable(b);
    }

    @FXML
    void boardTypeToggled(ActionEvent event) {
        RadioMenuItem clickedToggle = (RadioMenuItem) event.getTarget();
        if (clickedToggle == britishBoardRadio) {
            setPegs(SolitaireBoardType.BRITISH);
        } else { // clickedToggle == europeanBoardRadio
            setPegs(SolitaireBoardType.EUROPEAN);
        }
    }

    @FXML
    void endGameClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void newGameClicked(ActionEvent event) {
        setDisableBoardTypeRadios(false);
        setPegs(solitaire.getBoardType());
    }
}
