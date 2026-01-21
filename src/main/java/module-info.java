module com.example.solarsystemsimulation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.solarsystemsimulation to javafx.fxml;
    exports com.example.solarsystemsimulation;
}