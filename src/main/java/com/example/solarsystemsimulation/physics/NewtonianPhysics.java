package com.example.solarsystemsimulation.physics;

import com.example.solarsystemsimulation.model.CelestialBody;
import java.util.List;

/**
 * Implementacja wzorca Strategy - obliczenia grawitacyjne według mechaniki Newtona
 */
public class NewtonianPhysics implements PhysicsStrategy {
    // Stała grawitacyjna w jednostkach AU³/(M☉·day²)
    // G = 6.67430e-11 m³/(kg·s²)
    // Przekształcone do AU³/(M☉·day²) ≈ 2.95912208286e-4
    private static final double G = 2.95912208286e-4;

    @Override
    public Vector2D calculateAcceleration(CelestialBody body, List<CelestialBody> allBodies) {
        Vector2D totalAcceleration = Vector2D.zero();

        for (CelestialBody other : allBodies) {
            if (body == other) {
                continue; // Pomijamy samego siebie
            }

            // Wektor od body do other
            Vector2D direction = other.getPosition().subtract(body.getPosition());
            double distanceSquared = direction.magnitudeSquared();
            
            // Unikamy dzielenia przez zero dla bardzo bliskich obiektów
            if (distanceSquared < 1e-10) {
                continue;
            }

            double distance = Math.sqrt(distanceSquared);
            
            // F = G * m1 * m2 / r²
            // a = F / m1 = G * m2 / r²
            double accelerationMagnitude = G * other.getMass() / distanceSquared;
            
            // Wektor jednostkowy w kierunku other
            Vector2D accelerationDirection = direction.divide(distance);
            
            // Przyspieszenie w kierunku other
            Vector2D acceleration = accelerationDirection.multiply(accelerationMagnitude);
            
            totalAcceleration = totalAcceleration.add(acceleration);
        }

        return totalAcceleration;
    }

    @Override
    public void updateBody(CelestialBody body, double deltaTime) {
        // Metoda Verleta (lepsza stabilność numeryczna niż Euler)
        // v(t+Δt) = v(t) + a(t) * Δt
        Vector2D newVelocity = body.getVelocity().add(
            body.getAcceleration().multiply(deltaTime)
        );
        
        // x(t+Δt) = x(t) + v(t) * Δt + 0.5 * a(t) * Δt²
        Vector2D positionChange = body.getVelocity().multiply(deltaTime)
            .add(body.getAcceleration().multiply(0.5 * deltaTime * deltaTime));
        Vector2D newPosition = body.getPosition().add(positionChange);
        
        body.setVelocity(newVelocity);
        body.setPosition(newPosition);
    }
}
