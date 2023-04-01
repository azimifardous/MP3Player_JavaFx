module com.mp3player_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mp3player_javafx to javafx.fxml;
    exports com.mp3player_javafx;
}