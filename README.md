# EVDictionary-Java
English-Vietnamese Dictionary with Online Google Translate

Các thành viên trong nhóm UET K64-K2:
- Hoàng Hải Nam
- Đinh Ngọc Sơn
- Nguyễn Tiến Nghĩa

Phiên bản sử dụng: JDK 17

Hướng dẫn
1. Sau khi giải nén (hoặc _pull_), chọn __File\Project Structure\Modules\Dependencies\\+__
2. Điều hướng tới __external resource\freetts\lib__ và __external resource\mysql-connector\mysql-connector.jar__
3. Run
4. P/S: Nếu IDE không tự nhận JavaFX, chọn __Project Structure\Library\\+\external resource\javafx\lib__ và tại IDE chọn __Run\Run with parameter\Modify option\Add VM Option__, cuối cùng điền _--module-path="PATH" --add-modules=javafx.controls,javafx.fxml_, với _PATH_ là đường dẫn (absolute path) tới _lib_ của JavaFX ở trên.

Framework: SceneBuilder, JavaFX

Technology: MySQL, JDBC, FreeTTS, REST

![](src/main/resources/com/example/dictionary/images/EVD_Main.png)

## Mô tả chức năng

- Từ điển tuân theo mô hình CRUD với các chức năng sau:

![](src/main/resources/com/example/dictionary/images/info24.png) Tra từ tiếng Anh, hiển thị cách phát âm, loại từ và nghĩa trong tiếng Việt

![](src/main/resources/com/example/dictionary/images/icons8-trash-24.png) Thêm từ mới hoặc xóa từ trong tiếng Anh

![](src/main/resources/com/example/dictionary/images/info24.png) Cho phép cập nhật (hay sửa) loại từ và nghĩa trong tiếng Anh

![](src/main/resources/com/example/dictionary/images/icons8-speaker-24.png) Phát âm từ tiếng Anh (English US)

![](src/main/resources/com/example/dictionary/images/info24.png) Dịch câu tiếng Anh sang tiếng Việt sử dụng Google Translate API (giới hạn 1024 ký tự)

- Hướng phát triển tiếp theo: Thêm ngôn ngữ dịch, tích hợp cho website, dịch file, dịch ảnh.
