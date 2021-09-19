package com.example.solitaire;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;

import java.io.IOException;

public class PegContextMenu extends ContextMenu {
    public PegContextMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("peg-context_menu-view.fxml"));
        loader.setRoot(this);
        loader.load();
    }
}
