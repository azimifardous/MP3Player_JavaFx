package com.mp3player_javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class Controller implements Initializable {

    @FXML
    private Label songLabel;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;

    private File directory;
    private File[] files;
    private Media media;
    private MediaPlayer mediaPlayer;
    private ArrayList<File> songs;
    private int songNumber;
    private int[] speed = {25, 50, 75, 100, 125, 150, 175, 200};
    private Timer timer;
    private TimerTask task;
    private boolean isRunning;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();
        directory = new File("music");
        files = directory.listFiles();

        if (files != null)
            for (File file : files)
                songs.add(file);

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());

        for (int i = 0; i < speed.length; i++)
            speedBox.getItems().add(Integer.toString(speed[i]) + "%");

        speedBox.setOnAction(this::changeSpeed);
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
    }
    public void playMedia() {
        beginTimer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }
    public void pauseMedia() {
        cancelTimer();
        mediaPlayer.pause();
    }
    public void resetMedia() {
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }
    public void prevMedia() {
        if (songNumber > 0)
            songNumber--;
        else
            songNumber = songs.size() - 1;

        if (isRunning)
            cancelTimer();

        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        playMedia();
    }
    public void nextMedia() {
        if (songNumber < songs.size() - 1)
            songNumber++;
        else
            songNumber = 0;

        if (isRunning)
            cancelTimer();

        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        playMedia();
    }
    public void changeSpeed(ActionEvent event) {
        if (speedBox.getValue() != null)
            mediaPlayer.setRate(Integer.parseInt
                    (speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        else
            mediaPlayer.setRate(1);
    }
    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                isRunning = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                double progress = (current / end) * 1000;
                songProgressBar.setProgress(progress);
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    public void cancelTimer() {
        isRunning = false;
        timer.cancel();
    }
}