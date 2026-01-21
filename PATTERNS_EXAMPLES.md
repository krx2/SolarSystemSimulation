# Przykłady zastosowania wzorców projektowych

## 1. Strategy - Strategia obliczeń fizycznych

### Problem
System potrzebuje różnych algorytmów obliczeń fizycznych (Newtona, relatywistyczne, etc.)

### Rozwiązanie
```java
// Interface strategii
public interface PhysicsStrategy {
    Vector2D calculateAcceleration(CelestialBody body, List<CelestialBody> allBodies);
    void updateBody(CelestialBody body, double deltaTime);
}

// Konkretna strategia - fizyka Newtona
public class NewtonianPhysics implements PhysicsStrategy {
    private static final double G = 2.95912208286e-4;
    
    @Override
    public Vector2D calculateAcceleration(CelestialBody body, List<CelestialBody> allBodies) {
        // Implementacja prawa grawitacji Newtona
        Vector2D totalAcceleration = Vector2D.zero();
        for (CelestialBody other : allBodies) {
            if (body != other) {
                Vector2D direction = other.getPosition().subtract(body.getPosition());
                double distanceSquared = direction.magnitudeSquared();
                double accelerationMagnitude = G * other.getMass() / distanceSquared;
                totalAcceleration = totalAcceleration.add(
                    direction.normalize().multiply(accelerationMagnitude)
                );
            }
        }
        return totalAcceleration;
    }
}

// Użycie
Simulation simulation = new Simulation(new NewtonianPhysics());
```

### Korzyści
- Łatwa zmiana algorytmu fizyki
- Testowanie różnych podejść
- Zgodność z Open/Closed Principle

---

## 2. State - Stan symulacji

### Problem
Symulacja może być w różnych stanach (uruchomiona, zatrzymana) z różnym zachowaniem

### Rozwiązanie
```java
// Interface stanu
public interface SimulationState {
    void start();
    void stop();
    void reset();
    void update();
}

// Stan uruchomiony
public class RunningState implements SimulationState {
    private final Simulation simulation;
    
    @Override
    public void update() {
        simulation.updatePhysics(); // Aktualizuje fizykę
    }
    
    @Override
    public void stop() {
        simulation.setState(new StoppedState(simulation));
        simulation.notifyObserversStopped();
    }
}

// Stan zatrzymany
public class StoppedState implements SimulationState {
    @Override
    public void update() {
        // Nic nie robi - symulacja zatrzymana
    }
    
    @Override
    public void start() {
        simulation.setState(new RunningState(simulation));
        simulation.notifyObserversStarted();
    }
}
```

### Korzyści
- Jasna separacja zachowań dla różnych stanów
- Eliminacja warunkowych instrukcji if/else
- Łatwe dodawanie nowych stanów (np. Paused, Rewinding)

---

## 3. Observer - Powiadamianie o zmianach

### Problem
GUI musi reagować na zmiany w symulacji bez ścisłego powiązania

### Rozwiązanie
```java
// Interface observera
public interface SimulationObserver {
    void onSimulationUpdate();
    void onSimulationStarted();
    void onSimulationStopped();
    void onSimulationReset();
}

// Subject (Observable)
public class Simulation {
    private final List<SimulationObserver> observers = new ArrayList<>();
    
    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }
    
    public void notifyObserversUpdate() {
        for (SimulationObserver observer : observers) {
            observer.onSimulationUpdate();
        }
    }
}

// Konkretny observer
public class SimulationController implements SimulationObserver {
    @Override
    public void onSimulationUpdate() {
        renderer.render(simulation.getBodies()); // Odświeża widok
    }
    
    @Override
    public void onSimulationStarted() {
        updateButtonStates(true); // Zmienia stan przycisków
    }
}
```

### Korzyści
- Luźne powiązanie między logiką a GUI
- Możliwość wielu obserwatorów (np. logger, statystyki)
- Zgodność z Dependency Inversion Principle

---

## 4. Memento - Zapisywanie stanu

### Problem
Potrzeba zapisania i przywrócenia stanu symulacji (Reset, Save/Load)

### Rozwiązanie
```java
// Memento dla pojedynczego ciała
public static class CelestialBodyMemento implements Serializable {
    private final Vector2D position;
    private final Vector2D velocity;
    private final Vector2D acceleration;
    
    public CelestialBodyMemento(Vector2D position, Vector2D velocity, 
                                Vector2D acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }
}

// Originator
public class CelestialBody {
    public CelestialBodyMemento saveToMemento() {
        return new CelestialBodyMemento(position, velocity, acceleration);
    }
    
    public void restoreFromMemento(CelestialBodyMemento memento) {
        this.position = memento.getPosition();
        this.velocity = memento.getVelocity();
        this.acceleration = memento.getAcceleration();
    }
}

// Caretaker
public class SimulationController {
    private Simulation.SimulationMemento initialState;
    
    public void initialize() {
        initialState = simulation.saveToMemento(); // Zapisz stan początkowy
    }
    
    public void handleReset() {
        simulation.restoreFromMemento(initialState); // Przywróć
    }
}
```

### Korzyści
- Enkapsulacja stanu wewnętrznego
- Łatwe zapisywanie i przywracanie
- Wsparcie dla undo/redo

---

## 5. Adapter - Adaptacja danych XML

### Problem
Dane z Wikipedii są w formacie XML/HTML i wymagają konwersji

### Rozwiązanie
```java
public class PlanetDataParser {
    // Adaptuje dane z XML do modelu aplikacji
    public static List<CelestialBody> parsePlanetsFromXML(String xmlPath) {
        List<CelestialBody> bodies = new ArrayList<>();
        
        // Parsowanie XML
        Document doc = builder.parse(new File(xmlPath));
        String content = doc.getElementsByTagName("text").item(0).getTextContent();
        
        // Ekstrakcja danych
        String planetName = extractPlanetName(row);
        double diameter = extractDiameter(row);
        
        // Konwersja do modelu
        double orbitalDistance = ORBITAL_DISTANCES.get(planetName);
        double orbitalVelocity = Math.sqrt(1.0 / orbitalDistance) * 0.01720209895;
        
        CelestialBody planet = new CelestialBody(
            planetName,
            massRelativeToEarth,
            diameter / 2.0,
            color,
            new Vector2D(orbitalDistance, 0),
            new Vector2D(0, orbitalVelocity)
        );
        
        return bodies;
    }
}
```

### Korzyści
- Separacja źródła danych od modelu
- Łatwa zmiana źródła danych
- Konwersja jednostek w jednym miejscu

---

## 6. Decorator - Rozszerzenie renderowania

### Problem
GraphicsContext wymaga dodatkowych funkcji (skala, orbity, legendy)

### Rozwiązanie
```java
public class SimulationRenderer {
    private final GraphicsContext gc; // Dekorowany obiekt
    private final double scale;
    
    public void render(List<CelestialBody> bodies) {
        clear();
        drawOrbits(bodies);      // Dodatkowa funkcjonalność
        for (CelestialBody body : bodies) {
            drawBody(body);       // Użycie gc z dodatkowymi funkcjami
        }
        drawLegend(bodies);      // Dodatkowa funkcjonalność
    }
    
    private void drawBody(CelestialBody body) {
        Vector2D pos = body.getPosition();
        double screenX = centerX + pos.getX() * scale; // Konwersja skali
        double screenY = centerY + pos.getY() * scale;
        gc.fillOval(screenX - size/2, screenY - size/2, size, size);
    }
}
```

### Korzyści
- Rozszerzenie funkcjonalności bez modyfikacji GraphicsContext
- Separacja logiki renderowania
- Możliwość dodawania nowych elementów wizualnych

---

## 7. Iterator (wbudowany)

### Problem
Bezpieczna iteracja po kolekcji ciał niebieskich

### Rozwiązanie
```java
public class Simulation {
    private final List<CelestialBody> bodies;
    
    public List<CelestialBody> getBodies() {
        return new ArrayList<>(bodies); // Defensive copy
    }
}

// Użycie
for (CelestialBody body : simulation.getBodies()) {
    // Bezpieczna iteracja
    Vector2D acceleration = physicsStrategy.calculateAcceleration(body, allBodies);
}
```

### Korzyści
- Bezpieczna iteracja
- Hermetyzacja struktury danych
- Niemożność przypadkowej modyfikacji kolekcji

---

## Podsumowanie zastosowania SOLID

### Single Responsibility
Każda klasa ma jedną odpowiedzialność:
- `Vector2D` - operacje wektorowe
- `NewtonianPhysics` - obliczenia fizyczne
- `SimulationRenderer` - renderowanie
- `PlanetDataParser` - parsowanie danych

### Open/Closed
- Nowe strategie fizyki bez modyfikacji Simulation
- Nowe stany bez modyfikacji logiki
- Nowi obserwatorzy bez zmiany Simulation

### Liskov Substitution
- Wszystkie PhysicsStrategy wymienne
- Wszystkie SimulationState wymienne
- Wszystkie SimulationObserver wymienne

### Interface Segregation
- Małe, specyficzne interfejsy
- Klasy implementują tylko potrzebne metody

### Dependency Inversion
- Simulation zależy od abstrakcji (PhysicsStrategy, SimulationState)
- GUI zależy od interfejsu (SimulationObserver)
- Brak bezpośrednich zależności od konkretnych implementacji
