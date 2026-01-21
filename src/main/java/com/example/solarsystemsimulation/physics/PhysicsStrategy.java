package com.example.solarsystemsimulation.physics;

import com.example.solarsystemsimulation.model.CelestialBody;
import java.util.List;

/**
 * Wzorzec Strategy - interfejs dla różnych implementacji obliczeń fizycznych
 */
public interface PhysicsStrategy {
    /**
     * Oblicza przyspieszenie dla danego ciała niebieskiego
     */
    Vector2D calculateAcceleration(CelestialBody body, List<CelestialBody> allBodies);
    
    /**
     * Aktualizuje pozycję i prędkość ciała na podstawie przyspieszenia
     */
    void updateBody(CelestialBody body, double deltaTime);
}
