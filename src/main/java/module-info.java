module com.example.solitaire {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.solitaire to javafx.fxml;
    exports com.example.solitaire;
    exports com.example.solitaire.backend;
    opens com.example.solitaire.backend to javafx.fxml;
}