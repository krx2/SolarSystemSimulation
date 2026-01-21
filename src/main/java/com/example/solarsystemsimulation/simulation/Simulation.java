package com.example.solarsystemsimulation.simulation;

import com.example.solarsystemsimulation.model.CelestialBody;
import com.example.solarsystemsimulation.physics.PhysicsStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Główna klasa symulacji - wzorzec State i Observer
 */
public class Simulation {
    private final List<CelestialBody> bodies;
    private final List<SimulationObserver> observers;
    private final PhysicsStrategy physicsStrategy;
    private SimulationState state;
    private double timeStep; // w dniach
    private double speedMultiplier; // mnożnik prędkości

    public Simulation(PhysicsStrategy physicsStrategy) {
        this.bodies = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.physicsStrategy = physicsStrategy;
        this.state = new StoppedState(this);
        this.timeStep = 0.001; // ~2.4 minuty w czasie rzeczywistym
        this.speedMultiplier = 1.0;
    }

    public void addBody(CelestialBody body) {
        bodies.add(body);
    }

    public List<CelestialBody> getBodies() {
        return new ArrayList<>(bodies);
    }

    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(SimulationObserver observer) {
        observers.remove(observer);
    }

    public void setState(SimulationState state) {
        this.state = state;
    }

    public SimulationState getState() {
        return state;
    }

    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    // Metody delegowane do stanu (wzorzec State)
    public void start() {
        state.start();
    }

    public void stop() {
        state.stop();
    }

    public void reset() {
        state.reset();
    }

    public void update() {
        state.update();
    }

    /**
     * Aktualizacja fizyki - oblicza nową pozycję i prędkość dla wszystkich ciał
     */
    public void updatePhysics() {
        // Oblicz przyspieszenie dla wszystkich ciał
        List<CelestialBody> bodiesCopy = new ArrayList<>(bodies);
        for (CelestialBody body : bodies) {
            if (!body.isSun()) { // Słońce nie porusza się
                var acceleration = physicsStrategy.calculateAcceleration(body, bodiesCopy);
                body.setAcceleration(acceleration);
            }
        }

        // Aktualizuj pozycje i prędkości
        double effectiveTimeStep = timeStep * speedMultiplier;
        for (CelestialBody body : bodies) {
            if (!body.isSun()) {
                physicsStrategy.updateBody(body, effectiveTimeStep);
            }
        }

        notifyObserversUpdate();
    }

    public void notifyObserversUpdate() {
        for (SimulationObserver observer : observers) {
            observer.onSimulationUpdate();
        }
    }

    public void notifyObserversStarted() {
        for (SimulationObserver observer : observers) {
            observer.onSimulationStarted();
        }
    }

    public void notifyObserversStopped() {
        for (SimulationObserver observer : observers) {
            observer.onSimulationStopped();
        }
    }

    public void notifyObserversReset() {
        for (SimulationObserver observer : observers) {
            observer.onSimulationReset();
        }
    }

    /**
     * Zapisuje stan symulacji (wzorzec Memento)
     */
    public SimulationMemento saveToMemento() {
        List<CelestialBody.CelestialBodyMemento> bodyMementos = new ArrayList<>();
        for (CelestialBody body : bodies) {
            bodyMementos.add(body.saveToMemento());
        }
        return new SimulationMemento(bodyMementos, speedMultiplier);
    }

    /**
     * Przywraca stan symulacji (wzorzec Memento)
     */
    public void restoreFromMemento(SimulationMemento memento) {
        List<CelestialBody.CelestialBodyMemento> bodyMementos = memento.getBodyMementos();
        for (int i = 0; i < bodies.size() && i < bodyMementos.size(); i++) {
            bodies.get(i).restoreFromMemento(bodyMementos.get(i));
        }
        this.speedMultiplier = memento.getSpeedMultiplier();
    }

    /**
     * Klasa wewnętrzna dla Memento
     */
    public static class SimulationMemento implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private final List<CelestialBody.CelestialBodyMemento> bodyMementos;
        private final double speedMultiplier;

        public SimulationMemento(List<CelestialBody.CelestialBodyMemento> bodyMementos, 
                                 double speedMultiplier) {
            this.bodyMementos = new ArrayList<>(bodyMementos);
            this.speedMultiplier = speedMultiplier;
        }

        public List<CelestialBody.CelestialBodyMemento> getBodyMementos() {
            return new ArrayList<>(bodyMementos);
        }

        public double getSpeedMultiplier() {
            return speedMultiplier;
        }
    }
}
