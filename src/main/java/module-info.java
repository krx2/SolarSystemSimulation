module com.example.solarsystemsimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;

    opens com.example.solarsystemsimulation to javafx.fxml;
    opens com.example.solarsystemsimulation.ui to javafx.fxml;
    
    exports com.example.solarsystemsimulation;
    exports com.example.solarsystemsimulation.ui;
    exports com.example.solarsystemsimulation.model;
    exports com.example.solarsystemsimulation.physics;
    exports com.example.solarsystemsimulation.simulation;
    exports com.example.solarsystemsimulation.data;
    exports com.example.solarsystemsimulation.persistence;
}
