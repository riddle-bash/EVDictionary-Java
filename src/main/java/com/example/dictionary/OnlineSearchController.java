package com.example.dictionary;

import com.example.dictionary.processData.Translator;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OnlineSearchController implements Initializable {

  @FXML
  ComboBox<String> inputLang, outputLang;
  @FXML
  TextArea inputText, outputText;
  String chosenInputLang, chosenOutputLang;
  private Stage stage;
  private Scene scene;
  private Parent root;
  @FXML
  private Button onlinescene, mainscene, addscene, updatescene, aboutUs;
  @FXML
  private ImageView loadingIcon;

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
    ObservableList<String> observableList = FXCollections.observableArrayList("Tiếng Anh",
        "Tiếng Việt");
    inputLang.setItems(observableList);
    outputLang.setItems(observableList);

    inputLang.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        String chosen = inputLang.getSelectionModel().getSelectedItem();

        if (chosen.equals("Tiếng Anh")) {
          chosenInputLang = "en";
          outputLang.getSelectionModel().selectLast();
        } else if (chosen.equals("Tiếng Việt")) {
          chosenInputLang = "vi";
          outputLang.getSelectionModel().selectFirst();
        } else {
          chosenInputLang = null;
        }

        // TRACK
        System.out.println("You choose input: " + chosen);
      }
    });

    outputLang.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        String chosen = outputLang.getSelectionModel().getSelectedItem();

        if (chosen.equals("Tiếng Anh")) {
          chosenOutputLang = "en";
          inputLang.getSelectionModel().selectLast();
        } else if (chosen.equals("Tiếng Việt")) {
          chosenOutputLang = "vi";
          inputLang.getSelectionModel().selectFirst();
        } else {
          chosenOutputLang = null;
        }

        // TRACK
        System.out.println("You choose output: " + chosen);
      }
    });
  }

  @FXML
  public void actionTranslate() {
    if (chosenInputLang == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn cần chọn ngôn ngữ vào!");

      alert.show();
    } else if (chosenOutputLang == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn cần chọn ngôn ngữ ra!");

      alert.show();
    } else if (Objects.equals(inputText.getText(), "") || inputText.getText() == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn cần nhập từ cần dịch!");

      alert.show();
    } else if (Objects.equals(chosenOutputLang, chosenInputLang)) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn không thể dịch từ 2 ngôn ngữ giống nhau!");

      alert.show();
    } else {
      String[] inputTextList = inputText.getText().split("\n");
      StringBuilder outputTextList = new StringBuilder();

      // TRACK
      System.out.println("Search online successfully:");

      // FUNCTION: Translate each separate words
      for (String s : inputTextList) {
        String translated = Translator.translateOnline(chosenInputLang,
            chosenOutputLang, s.trim());
        translated = translated.replace("&quot;", "\"");
        translated = translated.replace("&#39;", "'");
        outputTextList.append(translated).append("\n");

        System.out.println("\t" + s + " - " + translated);
      }
      outputText.setText(outputTextList.toString());
    }
  }

  @FXML
  public void actionReset() {
    inputText.setText("");
    outputText.setText("");
  }
}
