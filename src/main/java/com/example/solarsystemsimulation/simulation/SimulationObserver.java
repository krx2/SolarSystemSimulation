package com.example.solarsystemsimulation.simulation;

/**
 * Wzorzec Observer - interfejs dla obserwatorów symulacji
 */
public interface SimulationObserver {
    /**
     * Wywoływane przy każdej aktualizacji symulacji
     */
    void onSimulationUpdate();
    
    /**
     * Wywoływane gdy symulacja jest uruchamiana
     */
    void onSimulationStarted();
    
    /**
     * Wywoływane gdy symulacja jest zatrzymywana
     */
    void onSimulationStopped();
    
    /**
     * Wywoływane gdy symulacja jest resetowana
     */
    void onSimulationReset();
}
