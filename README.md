# Symulacja Układu Słonecznego - Dokumentacja

## Opis projektu
Interaktywna symulacja fizyczna układu słonecznego w 2D wykorzystująca mechanikę klasyczną Newtona. 
Projekt został zaimplementowany zgodnie z zasadami SOLID oraz wykorzystuje następujące wzorce projektowe.

## Wzorce projektowe

### 1. **Strategy (Strategia)**
- **Gdzie**: `PhysicsStrategy` i `NewtonianPhysics`
- **Cel**: Umożliwienie łatwej zmiany algorytmu obliczeń fizycznych
- **Implementacja**: 
  - Interface `PhysicsStrategy` definiuje kontrakt dla obliczeń fizycznych
  - `NewtonianPhysics` implementuje obliczenia grawitacyjne według mechaniki Newtona
  - Możliwość łatwego dodania innych strategii (np. relatywistyczne obliczenia)

### 2. **State (Stan)**
- **Gdzie**: `SimulationState`, `RunningState`, `StoppedState`
- **Cel**: Zarządzanie stanem symulacji (uruchomiona/zatrzymana)
- **Implementacja**:
  - Interface `SimulationState` definiuje operacje dla różnych stanów
  - `RunningState` - symulacja działa, aktualizuje fizykę
  - `StoppedState` - symulacja zatrzymana, nie aktualizuje fizyki

### 3. **Observer (Obserwator)**
- **Gdzie**: `SimulationObserver` i `SimulationController`
- **Cel**: Oddzielenie logiki symulacji od warstwy prezentacji
- **Implementacja**:
  - Interface `SimulationObserver` definiuje metody powiadamiania
  - `SimulationController` implementuje observer i reaguje na zmiany
  - `Simulation` powiadamia obserwatorów o zmianach stanu

### 4. **Memento (Pamiątka)**
- **Gdzie**: `CelestialBody.CelestialBodyMemento`, `Simulation.SimulationMemento`
- **Cel**: Zapisywanie i przywracanie stanu symulacji
- **Implementacja**:
  - `CelestialBodyMemento` przechowuje stan pojedynczego ciała
  - `SimulationMemento` przechowuje stan całej symulacji
  - Użyte w funkcjach Reset, Save i Load

### 5. **Adapter**
- **Gdzie**: `PlanetDataParser`
- **Cel**: Adaptacja danych z XML Wikipedia do modelu aplikacji
- **Implementacja**:
  - Parsuje dane z XML
  - Konwertuje jednostki (km → AU, oblicza masy i prędkości orbitalne)
  - Dostarcza dane w formacie używanym przez `CelestialBody`

### 6. **Decorator (częściowo)**
- **Gdzie**: `SimulationRenderer`
- **Cel**: Rozszerzenie funkcjonalności GraphicsContext o specjalistyczne renderowanie
- **Implementacja**:
  - Opakowuje `GraphicsContext` 
  - Dodaje funkcjonalność rysowania orbit, skal, legend

### 7. **Iterator (wbudowany)**
- **Gdzie**: Kolekcje ciał niebieskich
- **Cel**: Bezpieczna iteracja po ciałach niebieskich
- **Implementacja**: Użycie standardowych kolekcji Java (List<CelestialBody>)

## Zasady SOLID

### Single Responsibility Principle (SRP)
- `Vector2D` - tylko operacje wektorowe
- `CelestialBody` - tylko reprezentacja ciała niebieskiego
- `NewtonianPhysics` - tylko obliczenia fizyczne
- `SimulationRenderer` - tylko renderowanie
- `PlanetDataParser` - tylko parsowanie danych

### Open/Closed Principle (OCP)
- Możliwość dodania nowych strategii fizyki bez modyfikacji istniejącego kodu
- Możliwość dodania nowych obserwatorów bez zmiany `Simulation`
- Nowe stany symulacji mogą być dodane bez modyfikacji logiki

### Liskov Substitution Principle (LSP)
- Wszystkie implementacje `PhysicsStrategy` są wymienne
- Wszystkie stany `SimulationState` zachowują się zgodnie z kontraktem
- Wszystkie obserwatory mogą być używane zamiennie

### Interface Segregation Principle (ISP)
- Interfejsy są małe i specyficzne (`PhysicsStrategy`, `SimulationState`, `SimulationObserver`)
- Klasy implementują tylko potrzebne interfejsy

### Dependency Inversion Principle (DIP)
- `Simulation` zależy od abstrakcji `PhysicsStrategy`, nie od konkretnej implementacji
- `Simulation` zależy od abstrakcji `SimulationState`, nie od konkretnych stanów
- GUI zależy od interfejsu `SimulationObserver`

## Struktura projektu

```
src/main/java/com/example/solarsystemsimulation/
├── SolarSystemApplication.java          # Główna klasa aplikacji
├── model/
│   └── CelestialBody.java              # Model ciała niebieskiego + Memento
├── physics/
│   ├── Vector2D.java                   # Klasa wektora 2D
│   ├── PhysicsStrategy.java            # Interface strategii fizyki
│   └── NewtonianPhysics.java           # Implementacja fizyki Newtona
├── simulation/
│   ├── Simulation.java                 # Główna logika symulacji
│   ├── SimulationState.java            # Interface stanu
│   ├── RunningState.java               # Stan uruchomiony
│   ├── StoppedState.java               # Stan zatrzymany
│   └── SimulationObserver.java         # Interface observera
├── ui/
│   ├── SimulationController.java       # Kontroler GUI (Observer)
│   └── SimulationRenderer.java         # Renderer (Decorator)
├── data/
│   └── PlanetDataParser.java           # Parser XML (Adapter)
└── persistence/
    └── SimulationPersistence.java      # Zapis/odczyt stanu
```

## Fizyka symulacji

### Obliczenia grawitacyjne
- Wykorzystanie prawa grawitacji Newtona: F = G * m1 * m2 / r²
- Stała grawitacyjna dostosowana do jednostek AU³/(M☉·day²)
- Metoda Verleta dla stabilności numerycznej

### Jednostki
- Odległość: AU (jednostki astronomiczne)
- Czas: dni
- Masa: relatywnie do masy Ziemi
- Prędkość: AU/dzień

### Kalibracja
- Na prędkości 1.0x: Ziemia okrąża Słońce w około 10 sekund
- Timestep: 0.001 dnia (~2.4 minuty)
- Prędkości: 0.5x, 1.0x, 4x, 16x

## Funkcjonalności GUI

1. **Start** - uruchamia symulację
2. **Stop** - zatrzymuje symulację
3. **Reset** - przywraca stan początkowy
4. **Zapisz** - zapisuje bieżący stan do pliku .sim
5. **Wczytaj** - wczytuje stan z pliku .sim
6. **Slider prędkości** - 4 poziomy prędkości symulacji

## Kompilacja i uruchomienie

### Wymagania
- Java 23
- Maven 3.x
- JavaFX 17.0.6

### Uruchomienie
```bash
mvn clean javafx:run
```

### Kompilacja
```bash
mvn clean compile
```

## Źródła danych
Dane planet pochodzą z Wikipedii (plik `api-result.xml`):
- Średnice planet
- Przyspieszenia grawitacyjne
- Dane użyte do obliczenia mas i prędkości orbitalnych

## Autor
Projekt stworzony jako demonstracja zasad SOLID i wzorców projektowych w kontekście 
symulacji fizycznej układu słonecznego.
