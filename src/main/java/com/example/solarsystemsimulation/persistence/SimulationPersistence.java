package com.example.solarsystemsimulation.persistence;

import com.example.solarsystemsimulation.simulation.Simulation;
import java.io.*;

/**
 * Klasa do zapisywania i wczytywania stanu symulacji (wzorzec Memento)
 */
public class SimulationPersistence {
    
    /**
     * Zapisuje stan symulacji do pliku
     */
    public static void saveSimulation(Simulation simulation, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            Simulation.SimulationMemento memento = simulation.saveToMemento();
            oos.writeObject(memento);
        }
    }
    
    /**
     * Wczytuje stan symulacji z pliku
     */
    public static void loadSimulation(Simulation simulation, String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Simulation.SimulationMemento memento = (Simulation.SimulationMemento) ois.readObject();
            simulation.restoreFromMemento(memento);
        }
    }
    
    /**
     * Sprawdza czy plik istnieje
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
}
