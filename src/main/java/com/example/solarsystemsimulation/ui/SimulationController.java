package com.example.solarsystemsimulation.ui;

import com.example.solarsystemsimulation.data.PlanetDataParser;
import com.example.solarsystemsimulation.model.CelestialBody;
import com.example.solarsystemsimulation.persistence.SimulationPersistence;
import com.example.solarsystemsimulation.physics.NewtonianPhysics;
import com.example.solarsystemsimulation.simulation.Simulation;
import com.example.solarsystemsimulation.simulation.SimulationObserver;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

/**
 * Kontroler GUI dla symulacji (wzorzec Observer)
 */
public class SimulationController implements SimulationObserver {
    @FXML
    private Canvas simulationCanvas;
    
    @FXML
    private Button startButton;
    
    @FXML
    private Button stopButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button loadButton;
    
    @FXML
    private Slider speedSlider;
    
    @FXML
    private Label speedLabel;
    
    private Simulation simulation;
    private SimulationRenderer renderer;
    private AnimationTimer animationTimer;
    private Simulation.SimulationMemento initialState;
    
    /**
     * Inicjalizacja kontrolera
     */
    @FXML
    public void initialize() {
        // Utworzenie symulacji z fizyką newtonowską
        simulation = new Simulation(new NewtonianPhysics());
        simulation.addObserver(this);
        
        // Wczytanie danych planet z XML
        loadPlanets();
        
        // Zapisanie stanu początkowego
        initialState = simulation.saveToMemento();
        
        // Utworzenie renderera
        renderer = new SimulationRenderer(
            simulationCanvas.getGraphicsContext2D(),
            simulationCanvas.getWidth(),
            simulationCanvas.getHeight()
        );
        
        // Timer animacji
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                simulation.update();
            }
        };
        
        // Konfiguracja slidera prędkości
        setupSpeedSlider();
        
        // Początkowe renderowanie
        renderer.render(simulation.getBodies());
        
        // Aktywacja przycisków
        updateButtonStates(false);
    }
    
    /**
     * Wczytuje planety z pliku XML lub używa domyślnych
     */
    private void loadPlanets() {
        // Próba wczytania z pliku XML
        String xmlPath = "api-result.xml";
        List<CelestialBody> bodies;
        
        if (new File(xmlPath).exists()) {
            bodies = PlanetDataParser.parsePlanetsFromXML(xmlPath);
        } else {
            bodies = PlanetDataParser.getDefaultPlanets();
        }
        
        // Dodaj ciała do symulacji
        for (CelestialBody body : bodies) {
            if (body.getName().equals("Słońce")) {
                body.setIsSun(true);
            }
            simulation.addBody(body);
        }
    }
    
    /**
     * Konfiguruje slider prędkości z 4 poziomami
     */
    private void setupSpeedSlider() {
        speedSlider.setMin(0);
        speedSlider.setMax(3);
        speedSlider.setValue(1); // Domyślnie 1.0x
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(0);
        speedSlider.setSnapToTicks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        
        // Niestandardowe etykiety
        speedSlider.setLabelFormatter(new javafx.util.StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                switch (value.intValue()) {
                    case 0: return "0.5x";
                    case 1: return "1.0x";
                    case 2: return "4x";
                    case 3: return "16x";
                    default: return "";
                }
            }
            
            @Override
            public Double fromString(String string) {
                return 1.0;
            }
        });
        
        // Listener zmiany wartości
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSpeed(newVal.intValue());
        });
        
        updateSpeed(1);
    }
    
    /**
     * Aktualizuje prędkość symulacji
     */
    private void updateSpeed(int level) {
        double[] speeds = {0.5, 1.0, 4.0, 16.0};
        double speed = speeds[Math.min(level, speeds.length - 1)];
        simulation.setSpeedMultiplier(speed);
        speedLabel.setText(String.format("%.1fx", speed));
    }
    
    /**
     * Obsługa przycisku Start
     */
    @FXML
    private void handleStart() {
        simulation.start();
        animationTimer.start();
    }
    
    /**
     * Obsługa przycisku Stop
     */
    @FXML
    private void handleStop() {
        simulation.stop();
        animationTimer.stop();
    }
    
    /**
     * Obsługa przycisku Reset
     */
    @FXML
    private void handleReset() {
        simulation.stop();
        animationTimer.stop();
        simulation.restoreFromMemento(initialState);
        simulation.reset();
        renderer.render(simulation.getBodies());
    }
    
    /**
     * Obsługa przycisku Save
     */
    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz stan symulacji");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Pliki symulacji", "*.sim")
        );
        
        Stage stage = (Stage) saveButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            try {
                SimulationPersistence.saveSimulation(simulation, file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Stan symulacji został zapisany.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się zapisać stanu: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obsługa przycisku Load
     */
    @FXML
    private void handleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj stan symulacji");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Pliki symulacji", "*.sim")
        );
        
        Stage stage = (Stage) loadButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            try {
                SimulationPersistence.loadSimulation(simulation, file.getAbsolutePath());
                renderer.render(simulation.getBodies());
                showAlert(Alert.AlertType.INFORMATION, "Sukces", "Stan symulacji został wczytany.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się wczytać stanu: " + e.getMessage());
            }
        }
    }
    
    /**
     * Aktualizuje stan przycisków
     */
    private void updateButtonStates(boolean isRunning) {
        startButton.setDisable(isRunning);
        stopButton.setDisable(!isRunning);
    }
    
    /**
     * Wyświetla dialog z alertem
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Implementacja SimulationObserver
    
    @Override
    public void onSimulationUpdate() {
        renderer.render(simulation.getBodies());
    }
    
    @Override
    public void onSimulationStarted() {
        updateButtonStates(true);
    }
    
    @Override
    public void onSimulationStopped() {
        updateButtonStates(false);
    }
    
    @Override
    public void onSimulationReset() {
        updateButtonStates(false);
    }
}
