package com.example.solarsystemsimulation.simulation;

/**
 * Wzorzec State - interfejs dla różnych stanów symulacji
 */
public interface SimulationState {
    void start();
    void stop();
    void reset();
    void update();
}
