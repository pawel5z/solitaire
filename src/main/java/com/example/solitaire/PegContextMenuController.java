package com.example.solitaire;

import com.example.solitaire.backend.Solitaire;
import com.example.solitaire.event_aggregator.EventAggregator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class PegContextMenuController implements EventHandler<PegRightClickedEvent> {
    private Circle pegClicked;

    @FXML
    private MenuItem jumpUpMenuItem;
    @FXML
    private MenuItem jumpDownMenuItem;
    @FXML
    private MenuItem jumpLeftMenuItem;
    @FXML
    private MenuItem jumpRightMenuItem;

    @FXML
    void initialize() {
        assert jumpUpMenuItem != null : "fx:id=\"jumpUpMenuItem\" was not injected: check your FXML file 'peg-context_menu-view.fxml'.";
        assert jumpDownMenuItem != null : "fx:id=\"jumpDownMenuItem\" was not injected: check your FXML file 'peg-context_menu-view.fxml'.";
        assert jumpLeftMenuItem != null : "fx:id=\"jumpLeftMenuItem\" was not injected: check your FXML file 'peg-context_menu-view.fxml'.";
        assert jumpRightMenuItem != null : "fx:id=\"jumpRightMenuItem\" was not injected: check your FXML file 'peg-context_menu-view.fxml'.";
    }

    @FXML
    void jumpUpClicked() {
        EventAggregator.getInstance().notify(
            ContextMenuJumpChosenEvent.class,
            new ContextMenuJumpChosenEvent(
                    GridPane.getRowIndex(pegClicked) - 2,
                    GridPane.getColumnIndex(pegClicked)));
    }

    @FXML
    void jumpDownClicked() {
        EventAggregator.getInstance().notify(
                ContextMenuJumpChosenEvent.class,
                new ContextMenuJumpChosenEvent(
                        GridPane.getRowIndex(pegClicked) + 2,
                        GridPane.getColumnIndex(pegClicked)));
    }

    @FXML
    void jumpLeftClicked() {
        EventAggregator.getInstance().notify(
                ContextMenuJumpChosenEvent.class,
                new ContextMenuJumpChosenEvent(
                        GridPane.getRowIndex(pegClicked),
                        GridPane.getColumnIndex(pegClicked) - 2));
    }

    @FXML
    void jumpRightClicked() {
        EventAggregator.getInstance().notify(
                ContextMenuJumpChosenEvent.class,
                new ContextMenuJumpChosenEvent(
                        GridPane.getRowIndex(pegClicked),
                        GridPane.getColumnIndex(pegClicked) + 2));
    }

    @Override
    public void handle(PegRightClickedEvent pegRightClickedEvent) {
        pegRightClickedEvent.consume();
        pegClicked = pegRightClickedEvent.getPegClicked();
        Solitaire solitaire = pegRightClickedEvent.getSolitaire();
        int r = GridPane.getRowIndex(pegClicked);
        int c = GridPane.getColumnIndex(pegClicked);

        jumpUpMenuItem.setDisable(solitaire.isMoveLegal(r, c, r - 2, c) == null);
        jumpDownMenuItem.setDisable(solitaire.isMoveLegal(r, c, r + 2, c) == null);
        jumpLeftMenuItem.setDisable(solitaire.isMoveLegal(r, c, r, c - 2) == null);
        jumpRightMenuItem.setDisable(solitaire.isMoveLegal(r, c, r, c + 2) == null);
    }
}
