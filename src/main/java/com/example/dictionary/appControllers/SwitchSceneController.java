package com.example.dictionary.appControllers;

import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SwitchSceneController {

  private static Stage stage;
  private static Scene scene;
  private static Parent root;

  public static void switchToOnlineScene(ActionEvent event) throws IOException {
    root = FXMLLoader.load(
        Objects.requireNonNull(SwitchSceneController.class.getResource("online-search-view.fxml")));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    scene.getStylesheets()
        .add(Objects.requireNonNull(
                SwitchSceneController.class.getResource("styles/onlineSearchStyle.css"))
            .toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

  public static void switchToMainScene(ActionEvent event) throws IOException {
    root = FXMLLoader.load(
        Objects.requireNonNull(SwitchSceneController.class.getResource("main-view.fxml")));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    scene.getStylesheets()
        .add(Objects.requireNonNull(SwitchSceneController.class.getResource("styles/style.css"))
            .toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

  public static void switchToAddScene(ActionEvent event) throws IOException {
    root = FXMLLoader.load(
        Objects.requireNonNull(SwitchSceneController.class.getResource("add-word-view.fxml")));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    scene.getStylesheets()
        .add(Objects.requireNonNull(
                SwitchSceneController.class.getResource("styles/addWordStyle.css"))
            .toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

  public static void switchToUpdateScene(ActionEvent event) throws IOException {
    root = FXMLLoader.load(
        Objects.requireNonNull(SwitchSceneController.class.getResource("update-word-view.fxml")));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    scene.getStylesheets()
        .add(Objects.requireNonNull(
                SwitchSceneController.class.getResource("styles/updateWordStyle.css"))
            .toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

  public static void aboutUs() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    Image image = new Image("https://img.icons8.com/fluency/48/000000/info.png");
    ImageView imageView = new ImageView(image);
    stage.getIcons().add(new Image("https://img.icons8.com/fluency/48/000000/info.png"));
    alert.setHeaderText(null);
    alert.setGraphic(imageView);
    alert.setTitle("Về chúng tôi");
    alert.setContentText(
        "Tác phẩm của: \nHoàng Hải Nam - K64K2 - Controller Dev\nĐinh Ngọc Sơn - K64K1 - View Dev\nNguyễn Tiến Nghĩa - K64K1");
    alert.show();
  }
}
