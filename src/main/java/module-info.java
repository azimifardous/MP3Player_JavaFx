module com.mp3player_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.mp3player_javafx to javafx.fxml;
    exports com.mp3player_javafx;
}