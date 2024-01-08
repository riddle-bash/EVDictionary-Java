package com.example.dictionary;

import com.example.dictionary.processData.ManageOnlineData;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddWordController implements Initializable {

  @FXML
  public TextField inputWordName, inputWordPronoun, inputWordType, inputWordMeaning;
  @FXML
  public TextArea listPreview;
  private Stage stage;
  private Scene scene;
  private Parent root;
  @FXML
  private Button onlinescene, mainscene, addscene, updatescene, aboutUs;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    onlinescene.setOnAction(e -> {
      try {
        SwitchSceneController.switchToOnlineScene(e);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
    mainscene.setOnAction(e -> {
      try {
        SwitchSceneController.switchToMainScene(e);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
    addscene.setOnAction(e -> {
      try {
        SwitchSceneController.switchToAddScene(e);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
    updatescene.setOnAction(e -> {
      try {
        SwitchSceneController.switchToUpdateScene(e);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
    aboutUs.setOnAction(e -> SwitchSceneController.aboutUs());
  }

  public void actionSave() {
    if (Objects.equals(inputWordName.getText(), "") || Objects.equals(inputWordType.getText(), "")
        || Objects.equals(inputWordPronoun.getText(), "") || Objects.equals(
        inputWordMeaning.getText(), "")) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn cần điền đầy đủ thông tin của từ cần thêm!");

      alert.show();
    } else {
      ArrayList<String> inputWordComponents = new ArrayList<>();

      inputWordComponents.add(inputWordName.getText().toLowerCase(Locale.ROOT));
      inputWordComponents.add(inputWordType.getText().toLowerCase(Locale.ROOT));
      inputWordComponents.add(inputWordPronoun.getText().toLowerCase(Locale.ROOT));
      inputWordComponents.add(inputWordMeaning.getText().toLowerCase(Locale.ROOT));

      ManageOnlineData.addNewWord(inputWordComponents);

      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Từ '" + inputWordName.getText() + "' đã được thêm thành công!");

      alert.show();
    }
  }

  public void actionPreview() {
    if (Objects.equals(inputWordName.getText(), "") || Objects.equals(inputWordType.getText(), "")
        || Objects.equals(inputWordPronoun.getText(), "") || Objects.equals(
        inputWordMeaning.getText(), "")) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn cần điền đầy đủ thông tin của từ cần thêm!");

      alert.show();
    } else {
      String name = inputWordName.getText();
      String type = inputWordType.getText();
      String pronoun = inputWordPronoun.getText();
      String meaning = inputWordMeaning.getText();

      String outputPreview = name + "\n" + pronoun + "\n\t" + type + "\n" + meaning;

      listPreview.setText(outputPreview);
    }
  }

  public void actionReset() {
    inputWordName.setText("");
    inputWordType.setText("");
    inputWordPronoun.setText("");
    inputWordMeaning.setText("");
  }
}
