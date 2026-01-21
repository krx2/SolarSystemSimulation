package com.example.solarsystemsimulation.simulation;

/**
 * Stan zatrzymanej symulacji (wzorzec State)
 */
public class StoppedState implements SimulationState {
    private final Simulation simulation;

    public StoppedState(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void start() {
        simulation.setState(new RunningState(simulation));
        simulation.notifyObserversStarted();
    }

    @Override
    public void stop() {
        // Już zatrzymana
    }

    @Override
    public void reset() {
        simulation.notifyObserversReset();
    }

    @Override
    public void update() {
        // Nie aktualizujemy gdy zatrzymana
    }
}
