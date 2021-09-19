package com.example.solitaire;

import com.example.solitaire.backend.Solitaire;
import com.example.solitaire.backend.SolitaireBoardType;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
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
    private ColorPicker tileColorPicker;
    @FXML
    private ColorPicker pegOutlineColorPicker;
    @FXML
    private ColorPicker pegFillingColorPicker;
    private PegContextMenu pegContextMenu;

    public SolitaireController() throws IOException {
        PegContextMenuController pcmController = new PegContextMenuController();
        pegContextMenu = new PegContextMenu(pcmController);
        pegContextMenu.addEventHandler(PegRightClickedEvent.ANY, pcmController);
    }

    @FXML
     void initialize() {
        // bind fields' sizes to grid's size
        for (Node node : board.getChildren()) {
            Rectangle rect = (Rectangle) node;
            rect.widthProperty().bind(board.widthProperty().divide(board.getColumnCount()));
            rect.heightProperty().bind(board.heightProperty().divide(board.getRowCount()));
        }

        // bind tiles' color to color pickers' value
        for (var node : board.getChildren())
            if (node instanceof Rectangle)
                ((Rectangle) node).fillProperty().bind(tileColorPicker.valueProperty());

        setPegs(SolitaireBoardType.BRITISH);
    }

    private void attemptMove(Circle peg, Rectangle field) {
        Pair<Integer, Integer> posToRemove = solitaire.move(
                GridPane.getRowIndex(markedPeg),
                GridPane.getColumnIndex(markedPeg),
                GridPane.getRowIndex(field),
                GridPane.getColumnIndex(field));
        if (posToRemove == null)
            return;
        board.getChildren().remove(markedPeg);
        board.add(markedPeg, GridPane.getColumnIndex(field), GridPane.getRowIndex(field));
        board.getChildren().remove(getPegByRowCol(posToRemove.getKey(), posToRemove.getValue()));
        setDisableBoardTypeRadios(!solitaire.isGameOver());
        updateStatusLabel();
    }

    @FXML
    void fieldClicked(MouseEvent event) {
        event.consume();
        if (markedPeg == null) {
            return;
        }
        Rectangle clickedField = (Rectangle) event.getTarget();
        attemptMove(markedPeg, clickedField);
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
                board.heightProperty().divide(board.getRowCount())).divide(2).multiply(0.8));
        peg.strokeProperty().bind(pegOutlineColorPicker.valueProperty());
        peg.fillProperty().bind(pegFillingColorPicker.valueProperty());
        peg.setStrokeWidth(5);

        peg.setOnMouseClicked(mouseEvent -> {
            mouseEvent.consume();
            pegContextMenu.hide();
            markedPeg = (Circle) mouseEvent.getTarget();
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                pegContextMenu.fireEvent(new PegRightClickedEvent(PegRightClickedEvent.ANY, peg, solitaire));
                pegContextMenu.show(peg, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });

        return peg;
    }

    private Rectangle getFieldByRowCol(int r, int c) {
        for (var field : board.getChildren())
            if (field instanceof Rectangle && GridPane.getRowIndex(field) == r && GridPane.getColumnIndex(field) == c)
                return (Rectangle) field;
        return null;
    }

    private Circle getPegByRowCol(int r, int c) {
        for (var peg : board.getChildren())
            if (peg instanceof Circle && GridPane.getRowIndex(peg) == r && GridPane.getColumnIndex(peg) == c)
                return (Circle) peg;
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

    @FXML
    void aboutGameClicked(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("about_game-view.fxml")).load()));
        stage.setTitle("About game");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(board.getScene().getWindow());
        stage.show();
    }

    @FXML
    void aboutAppClicked(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("about_app-view.fxml")).load()));
        stage.setTitle("About app");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(board.getScene().getWindow());
        stage.show();
    }
}
