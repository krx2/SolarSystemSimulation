package com.example.solarsystemsimulation.ui;

import com.example.solarsystemsimulation.model.CelestialBody;
import com.example.solarsystemsimulation.physics.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.List;

/**
 * Renderer do rysowania symulacji na Canvas (wzorzec Decorator - dekoruje GraphicsContext)
 */
public class SimulationRenderer {
    private final GraphicsContext gc;
    private final double width;
    private final double height;
    private final double scale; // skala AU -> piksele
    private final double centerX;
    private final double centerY;
    
    // Minimalna wielkość punktu dla małych planet
    private static final double MIN_PLANET_SIZE = 3.0;
    private static final double MAX_PLANET_SIZE = 30.0;
    
    public SimulationRenderer(GraphicsContext gc, double width, double height) {
        this.gc = gc;
        this.width = width;
        this.height = height;
        this.centerX = width / 2;
        this.centerY = height / 2;
        // Skala: 1 AU = około 60 pikseli (Neptun na 30 AU będzie daleko)
        this.scale = Math.min(width, height) / 70.0;
    }
    
    /**
     * Czyści canvas
     */
    public void clear() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }
    
    /**
     * Rysuje wszystkie ciała niebieskie
     */
    public void render(List<CelestialBody> bodies) {
        clear();
        
        // Rysuj orbity (tylko dla lepszej wizualizacji)
        drawOrbits(bodies);
        
        // Rysuj ciała
        for (CelestialBody body : bodies) {
            drawBody(body);
        }
        
        // Legenda
        drawLegend(bodies);
    }
    
    /**
     * Rysuje okręgi orbit
     */
    private void drawOrbits(List<CelestialBody> bodies) {
        gc.setStroke(Color.rgb(50, 50, 50));
        gc.setLineWidth(1);
        
        for (CelestialBody body : bodies) {
            if (!body.isSun()) {
                // Oblicz promień orbity (odległość od środka)
                Vector2D initialPos = body.getPosition();
                double orbitRadius = initialPos.magnitude() * scale;
                
                if (orbitRadius > 0 && orbitRadius < Math.max(width, height)) {
                    gc.strokeOval(
                        centerX - orbitRadius,
                        centerY - orbitRadius,
                        orbitRadius * 2,
                        orbitRadius * 2
                    );
                }
            }
        }
    }
    
    /**
     * Rysuje pojedyncze ciało niebieskie
     */
    private void drawBody(CelestialBody body) {
        Vector2D pos = body.getPosition();
        
        // Konwersja z AU na piksele
        double screenX = centerX + pos.getX() * scale;
        double screenY = centerY + pos.getY() * scale;
        
        // Rozmiar ciała - logarytmiczna skala dla lepszej wizualizacji
        double size;
        if (body.isSun()) {
            size = 20; // Słońce zawsze duże
        } else {
            // Rozmiar bazowany na promieniu (logarytmicznie)
            size = Math.log10(body.getRadius() + 1) * 2;
            size = Math.max(MIN_PLANET_SIZE, Math.min(MAX_PLANET_SIZE, size));
        }
        
        // Rysuj ciało
        gc.setFill(body.getColor());
        gc.fillOval(screenX - size/2, screenY - size/2, size, size);
        
        // Dodaj poświatę dla Słońca
        if (body.isSun()) {
            gc.setFill(Color.rgb(255, 255, 0, 0.3));
            gc.fillOval(screenX - size, screenY - size, size * 2, size * 2);
        }
    }
    
    /**
     * Rysuje legendę z nazwami planet
     */
    private void drawLegend(List<CelestialBody> bodies) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 12));
        
        int y = 20;
        for (CelestialBody body : bodies) {
            gc.fillText(body.getName(), 10, y);
            
            // Małe kółko z kolorem
            gc.setFill(body.getColor());
            gc.fillOval(body.getName().length() * 7 + 15, y - 8, 10, 10);
            gc.setFill(Color.WHITE);
            
            y += 20;
        }
    }
}
