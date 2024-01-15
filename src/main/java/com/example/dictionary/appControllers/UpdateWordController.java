package com.example.dictionary.appControllers;

import com.example.dictionary.processData.DBConfigs;
import com.example.dictionary.processData.LoadOnlineData;
import com.example.dictionary.processData.ManageOnlineData;
import com.example.dictionary.processData.Word;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateWordController implements Initializable {

  @FXML
  public TextField inputWordName, inputWordPronoun;
  @FXML
  public ComboBox inputWordType, inputWordMeaning;
  // CREATE
  ObservableList<String> observableListType;
  ObservableList<String> observableListMeaning;
  Word word;
  String currentWordName, currentWordPronoun;
  String currentWordType, currentWordMeaning;
  int currentTypeIndex, currentMeaningIndex;
  ArrayList<Integer> chosenType = new ArrayList<>();
  ArrayList<Integer> chosenMeaning = new ArrayList<>();
  // CREATE:
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
  private Button onlinescene, mainscene, addscene, updatescene, aboutUs;

  public static Word getData(String w) {
    Word word = new Word();

    String url = DBConfigs.url;
    String username = DBConfigs.username;
    String password = DBConfigs.password;

    try {
      Connection connection = DriverManager.getConnection(url, username, password);

      String sql = "SELECT * from data WHERE data.word = \"" + w + "\"";
      Statement statement = connection.createStatement();

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

      word.face_word = result.getString("word");
      word.pronunciation = result.getString("pronunciation");
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
    }
    return word;
  }

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
    if (searchbar.getText() != "") {
      // TRACK
      System.out.println("\nYou searched for: " + searchbar.getText());

      word = getData(searchbar.getText());

      resultView.setWrapText(true);
      resultView.setText(printWord(word));

      if (word != null) {
        inputWordName.setText(word.face_word);
        inputWordPronoun.setText(word.pronunciation);

        currentWordName = word.face_word;
        currentWordPronoun = word.pronunciation;

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
          if (word.types[i] != null) {
            arrayList.add(word.types[i]);
          } else {
            arrayList.add("<Trống>");
          }
        }

        observableListType = FXCollections.observableArrayList(arrayList);
        inputWordType.setItems(observableListType);
      }
    }
  }

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

    // FUNCTION: Print out the listSearch
    listSearch.setItems(filteredList);

    // FUNCTION: Search the word you clicked
    listSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        wordChosen = listSearch.getSelectionModel().getSelectedItem();

        if (wordChosen != null) {
          Word wordChosenFormat = getData(wordChosen.trim());

          word = wordChosenFormat;
          System.out.println("You clicked on: " + wordChosen);
          resultView.setWrapText(true);
          resultView.setText(printWord(wordChosenFormat));

          if (wordChosenFormat != null) {
            inputWordName.setText(wordChosenFormat.face_word);
            inputWordPronoun.setText(wordChosenFormat.pronunciation);

            currentWordName = word.face_word;
            currentWordPronoun = word.pronunciation;

            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
              if (wordChosenFormat.types[i] != null) {
                arrayList.add(wordChosenFormat.types[i]);
              } else {
                arrayList.add("<Trống>");
              }
            }

            // TRACK
            System.out.println("Danh sách loại từ là: " + arrayList);

            observableListType = FXCollections.observableArrayList(arrayList);
            inputWordType.setItems(observableListType);
          }
        }
      }
    });

    inputWordType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observableValue, Object o, Object t1) {
        if (word != null) {
          ArrayList<String> arrayList = new ArrayList<>();

          if (inputWordType.getSelectionModel().getSelectedIndex() != -1) {
            for (int index = 0; index < 5; index++) {
              String string = word.meanings[inputWordType.getSelectionModel()
                  .getSelectedIndex()][index];

              arrayList.add(Objects.requireNonNullElse(string, "<Trống>"));
            }

            chosenType.add(inputWordType.getSelectionModel().getSelectedIndex());

            System.out.println("Current type: " + chosenType);

            // TRACK
            System.out.println("Danh sách nghĩa là: " + arrayList);

            observableListMeaning = FXCollections.observableArrayList(arrayList);
            inputWordMeaning.setItems(observableListMeaning);
          }
        }
      }
    });

    inputWordMeaning.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observableValue, Object o, Object t1) {
        chosenMeaning.add(inputWordMeaning.getSelectionModel().getSelectedIndex());

        System.out.println("Current meaning: " + chosenMeaning);
      }
    });
  }

  public void updateInput() {
    Word wordUpdatedFormat = getData(currentWordName.trim());
    word = wordUpdatedFormat;
    resultView.setWrapText(true);
    resultView.setText(printWord(wordUpdatedFormat));

    ArrayList<String> arrayList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      if (wordUpdatedFormat.types[i] != null) {
        arrayList.add(wordUpdatedFormat.types[i]);
      } else {
        arrayList.add("<Trống>");
      }
    }

    // TRACK
    System.out.println("Danh sách loại từ là: " + arrayList);

    observableListType = FXCollections.observableArrayList(arrayList);
    inputWordType.setItems(observableListType);

    arrayList.clear();

    if (inputWordType.getSelectionModel().getSelectedIndex() != -1) {
      for (int index = 0; index < 5; index++) {
        String string = word.meanings[inputWordType.getSelectionModel().getSelectedIndex()][index];

        arrayList.add(Objects.requireNonNullElse(string, "<Trống>"));
      }

      chosenType.add(inputWordType.getSelectionModel().getSelectedIndex());

      System.out.println("Current type: " + chosenType);

      // TRACK
      System.out.println("Danh sách nghĩa là: " + arrayList);

      observableListMeaning = FXCollections.observableArrayList(arrayList);
      inputWordMeaning.setItems(observableListMeaning);

      arrayList.clear();
    }
  }

  public void actionSave() {
    if (Objects.equals(inputWordName.getText(), "")
        || inputWordType.getValue() == null
        || Objects.equals(inputWordType.getValue().toString(), "")
        || Objects.equals(inputWordPronoun.getText(), "")
        || inputWordMeaning.getValue() == null
        || Objects.equals(inputWordMeaning.getValue().toString(), "")) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn cần điền đầy đủ thông tin của từ cần thêm!");

      alert.show();
    } else if (Objects.equals(inputWordType.getValue().toString(), "<Trống>")
        || Objects.equals(inputWordMeaning.getValue().toString(), "<Trống>")
        || Objects.equals(inputWordType.getSelectionModel().getSelectedItem().toString(),
        "<Trống>")
        || Objects.equals(inputWordMeaning.getSelectionModel().getSelectedItem().toString(),
        "<Trống>")) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Thông báo");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Các thông tin của từ không thể để trống!");

      alert.show();
    } else {
      currentTypeIndex = chosenType.get(chosenType.size() - 1);

      if (chosenMeaning.size() == 1) {
        currentMeaningIndex = 0;
      } else if (chosenMeaning.get(chosenMeaning.size() - 1) != -1) {
        currentMeaningIndex = chosenMeaning.get(chosenMeaning.size() - 1);
      } else {
        currentMeaningIndex = chosenMeaning.get(chosenMeaning.size() - 2);
      }

      if (observableListType.get(currentTypeIndex) == "<Trống>") {
        currentWordType = "";
      } else {
        currentWordType = observableListType.get(currentTypeIndex);
      }

      if (observableListMeaning.get(currentMeaningIndex) == "<Trống>") {
        currentWordMeaning = "";
      } else {
        currentWordMeaning = observableListMeaning.get(currentMeaningIndex);
      }

      // FUNCTION: Replace old word with the new one
      ArrayList<String> inputWordComponentsCurrent = new ArrayList<>();

      inputWordComponentsCurrent.add(currentWordName.trim());
      inputWordComponentsCurrent.add(currentWordType);
      inputWordComponentsCurrent.add(currentWordPronoun.trim());
      inputWordComponentsCurrent.add(currentWordMeaning);

      ArrayList<String> inputWordComponentsNew = new ArrayList<>();

      inputWordComponentsNew.add(inputWordName.getText().trim());
      inputWordComponentsNew.add(inputWordType.getValue().toString());
      inputWordComponentsNew.add(inputWordPronoun.getText());
      inputWordComponentsNew.add(inputWordMeaning.getValue().toString());

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Xác nhận");
      alert.initStyle(StageStyle.UTILITY);
      alert.setHeaderText(null);
      alert.setContentText("Bạn có chắc chắn muốn sửa từ '"
          + inputWordName.getText() + "' không?");

      ButtonType yes = new ButtonType("Đồng ý", ButtonBar.ButtonData.YES);
      ButtonType no = new ButtonType("Quay lại", ButtonBar.ButtonData.CANCEL_CLOSE);

      alert.getButtonTypes().setAll(yes, no);

      Optional<ButtonType> confirm = alert.showAndWait();

      if (confirm.get() == yes) {
        ManageOnlineData.updateWord(inputWordComponentsCurrent, inputWordComponentsNew);

        updateInput();

        // FUNCTION: Notify the result and clear the temporary data
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.setContentText("Từ '" + inputWordName.getText() + "' đã được sửa thành công!");

        alert.show();

        wordObservableList = FXCollections.observableArrayList(
            LoadOnlineData.getListWords());
      }
    }
  }

  public void actionPreview() {
    if (inputWordName.getText() == ""
        || inputWordType.getValue() == null
        || inputWordType.getValue().toString() == ""
        || inputWordPronoun.getText() == ""
        || inputWordMeaning.getValue() == null
        || inputWordMeaning.getValue().toString() == "") {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Thông báo");

      alert.setHeaderText(null);
      alert.setContentText("Bạn cần điền đầy đủ thông tin của từ cần thêm!");

      alert.show();
    } else {
      String name = inputWordName.getText();
      String type = inputWordType.getValue().toString();
      String pronoun = inputWordPronoun.getText();
      String meaning = inputWordMeaning.getValue().toString();

      String outputPreview = name + "\n" + pronoun + "\n\t" + type + "\n" + meaning;

      resultView.setText(outputPreview);
    }
  }

  public void actionReset() {
    inputWordName.setText("");
    inputWordType.setValue("");
    inputWordPronoun.setText("");
    inputWordMeaning.setValue("");
  }

  @FXML
  public void actionClear() {
    searchbar.setText("");
  }
}
