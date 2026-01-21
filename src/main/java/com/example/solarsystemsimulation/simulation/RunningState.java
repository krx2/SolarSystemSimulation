package com.example.solarsystemsimulation.simulation;

/**
 * Stan uruchomionej symulacji (wzorzec State)
 */
public class RunningState implements SimulationState {
    private final Simulation simulation;

    public RunningState(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void start() {
        // Już uruchomiona
    }

    @Override
    public void stop() {
        simulation.setState(new StoppedState(simulation));
        simulation.notifyObserversStopped();
    }

    @Override
    public void reset() {
        simulation.setState(new StoppedState(simulation));
        simulation.notifyObserversReset();
    }

    @Override
    public void update() {
        simulation.updatePhysics();
    }
}
