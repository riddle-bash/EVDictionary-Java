package com.example.dictionary;

import java.util.Objects;

import com.example.dictionary.processData.LoadOnlineData;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    try {
      Parent root = FXMLLoader.load(
          Objects.requireNonNull(getClass().getResource("main-view.fxml")));
      Scene scene = new Scene(root);
      scene.getStylesheets()
          .add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
      stage.setTitle("Dictionary");
      stage.getIcons().add(new Image("https://img.icons8.com/dusk/64/000000/google-translate.png"));
      stage.setResizable(false);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {

    // CREATE: Import all data from database into an ArrayList
    LoadOnlineData.getListWordFromDB();
    launch();
  }
}