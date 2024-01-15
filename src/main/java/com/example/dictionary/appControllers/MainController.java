package com.example.dictionary.appControllers;

import com.example.dictionary.processData.DBConfigs;
import com.example.dictionary.processData.LoadOnlineData;
import com.example.dictionary.processData.ManageOnlineData;
import com.example.dictionary.processData.Word;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {

  // CREATE: Make a ArrayList observable (So list can change while we search)
  ObservableList<String> wordObservableList = FXCollections.observableArrayList(
      LoadOnlineData.getListWords());
  // CREATE:
  String wordChosen;
  private Stage stage;
  private Scene scene;
  private Parent root;
  @FXML
  private TextField searchbar;
  @FXML
  private ListView<String> listSearch;
  @FXML
  private TextArea resultView;
  @FXML
  private Word wordSearched;
  @FXML
  private Button onlinescene, mainscene, addscene, updatescene, aboutUs;

  /**
   * Function to get data from database when match the word(input)
   * @param w input which has type String
   * @return word which has type Word
   */
  public static Word getData(String w) {
    Word word = new Word();

    //call data from database
    String url = DBConfigs.url;
    String username = DBConfigs.username;
    String password = DBConfigs.password;

    try {
      //connection to database from local
      Connection connection = DriverManager.getConnection(url, username, password);

      //statement in mysql
      String sql = "SELECT word, pronunciation, type, meaning from data WHERE data.word = \"" + w + "\"";
      Statement statement = connection.createStatement();

      //call data and store it in result
      statement.executeQuery(sql);
      ResultSet result = statement.executeQuery(sql);

      int index_type = 0;
      int index_meaning = 0;

      if (!result.next()) {
        // TRACK
        System.out.println("Word is not available!");

        return null;
      }

      // TRACK
      System.out.println("Word found!");

      //only get face_word and pronunciation one time
      word.face_word = result.getString("word");
      word.pronunciation = result.getString("pronunciation");
      //get types max 3 time and meaning max 9 time
      word.types[index_type] = result.getString("type");
      word.meanings[index_type][index_meaning] = result.getString("meaning");

      while (result.next()) {
        String type = result.getString("type");
        String meaning = result.getString("meaning");

        if (!type.equals(word.types[index_type])) {
          index_type++;
          word.types[index_type] = type;
          index_meaning = 0;
        } else {
          index_meaning++;
        }
        word.meanings[index_type][index_meaning] = meaning;
      }

      statement.close();
      connection.close();
    } catch (Exception e) {
      System.out.println(e);
      word = null;
      e.printStackTrace();
    }
    return word;
  }

  /**
   * Function to convert from type Word to a String
   * @param word is input
   * @return The String which has modified
   */
  public static String printWord(Word word) {
    if (word == null) {
      return "Từ không tồn tại!";
    }

    String res = "";
    res += (word.face_word + "\n" + word.pronunciation + "\n");
    for (int j = 0; j < 5; j++) {
      if (word.types[j] != null) {
        res += ("\t" + word.types[j] + "\n");
        for (int k = 0; k < 5; k++) {
          if (word.meanings[j][k] != null) {
            res += (word.meanings[j][k] + "\n");
          } else {
            break;
          }
        }
      } else {
        break;
      }
    }
    return res;
  }

  @FXML
  protected void onEnter() {
    if (!Objects.equals(searchbar.getText(), "")) {
      // TRACK
      System.out.println("\nYou searched for: " + searchbar.getText());

      wordSearched = getData(searchbar.getText());

      resultView.setWrapText(true);
      resultView.setText(printWord(wordSearched));
    }
  }

  // FUNCTION: Suggest words as we search
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
    // CREATE: Start with empty list
    FilteredList<String> filteredList = new FilteredList<>(wordObservableList, b -> false);

    searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredList.setPredicate(String -> {

        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
          return false;
        }

        String searchKeyword = newValue.toLowerCase();

        return String.startsWith(searchKeyword);
      });
    });

    SortedList<String> sortedList = new SortedList<>(filteredList);

    // FUNCTION: Print out the listSearch
    listSearch.setItems(filteredList);

    // FUNCTION: Search the word you clicked
    listSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        wordChosen = listSearch.getSelectionModel().getSelectedItem();

        if (wordChosen != null) {
          wordSearched = getData(wordChosen.trim());
          System.out.println("You clicked on: " + wordChosen);
          resultView.setWrapText(true);
          resultView.setText(printWord(wordSearched));
        }
      }
    });
  }

  @FXML
  public void actionDelete() {
    if (wordSearched == null) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Cảnh báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn phải nhập tên từ cần xóa!");

      alert.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Xác nhận");

      alert.setHeaderText(null);
      alert.setContentText("Bạn có chắc chắn muốn xóa từ '"
          + wordSearched.face_word.trim() + "' không?");

      ButtonType yes = new ButtonType("Đồng ý", ButtonBar.ButtonData.YES);
      ButtonType no = new ButtonType("Quay lại", ButtonBar.ButtonData.CANCEL_CLOSE);

      alert.getButtonTypes().setAll(yes, no);

      Optional<ButtonType> confirm = alert.showAndWait();

      if (confirm.get() == yes) {
        ManageOnlineData.deleteWord(wordSearched.face_word.trim());
        wordObservableList.remove(wordSearched.face_word);
        resultView.setText("");
      }
    }
  }

  @FXML
  public void actionSpeak() {
    if (wordSearched == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn cần nhập hoặc chọn từ để phát âm!");

      alert.show();
    } else {
      String textToSpeech = wordSearched.face_word.trim();

      Voice voice;
      VoiceManager voiceManager;

      System.setProperty("freetts.voices",
          "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
      voiceManager = VoiceManager.getInstance();
      voice = voiceManager.getVoice("kevin");

      voice.allocate();
      voice.speak(textToSpeech);
      voice.deallocate();
    }
  }

  @FXML
  public void actionClear() {
    searchbar.setText("");
  }
}