package com.example.solarsystemsimulation.model;

import com.example.solarsystemsimulation.physics.Vector2D;
import javafx.scene.paint.Color;

/**
 * Klasa reprezentująca ciało niebieskie w symulacji
 */
public class CelestialBody {
    private final String name;
    private final double mass; // kg
    private final double radius; // km
    private final Color color;
    private Vector2D position; // AU (astronomical units)
    private Vector2D velocity; // AU/day
    private Vector2D acceleration; // AU/day²
    private boolean isSun;

    public CelestialBody(String name, double mass, double radius, Color color, 
                         Vector2D position, Vector2D velocity) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.color = color;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = Vector2D.zero();
        this.isSun = false;
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public boolean isSun() {
        return isSun;
    }

    public void setIsSun(boolean isSun) {
        this.isSun = isSun;
    }

    /**
     * Tworzy kopię ciała niebieskiego (wzorzec Memento)
     */
    public CelestialBodyMemento saveToMemento() {
        return new CelestialBodyMemento(position, velocity, acceleration);
    }

    /**
     * Przywraca stan z mementos (wzorzec Memento)
     */
    public void restoreFromMemento(CelestialBodyMemento memento) {
        this.position = memento.getPosition();
        this.velocity = memento.getVelocity();
        this.acceleration = memento.getAcceleration();
    }

    @Override
    public String toString() {
        return String.format("%s: pos=%s, vel=%s", name, position, velocity);
    }

    /**
     * Klasa wewnętrzna implementująca wzorzec Memento
     */
    public static class CelestialBodyMemento implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private final Vector2D position;
        private final Vector2D velocity;
        private final Vector2D acceleration;

        public CelestialBodyMemento(Vector2D position, Vector2D velocity, Vector2D acceleration) {
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
        }

        public Vector2D getPosition() {
            return position;
        }

        public Vector2D getVelocity() {
            return velocity;
        }

        public Vector2D getAcceleration() {
            return acceleration;
        }
    }
}
