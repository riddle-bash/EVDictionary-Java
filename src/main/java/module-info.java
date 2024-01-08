module com.example.dictionary {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires javafx.graphics;
  requires freetts;

  opens com.example.dictionary to javafx.fxml;
  exports com.example.dictionary;
}