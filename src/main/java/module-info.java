module com.example.project2oop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project2oop to javafx.fxml;
    exports com.example.project2oop;
    opens part2_2_inheritance to javafx.fxml;
    exports part2_2_inheritance;
}