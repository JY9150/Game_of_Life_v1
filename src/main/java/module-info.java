module com.example.game_of_life_v1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.game_of_life_v1 to javafx.fxml;
    exports com.example.game_of_life_v1;
}