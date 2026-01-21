# Szybki Start - Symulacja Układu Słonecznego

## Wymagania
- **Java 23** lub nowszy
- **Maven 3.x**
- **JavaFX 17.0.6** (pobierany automatycznie przez Maven)

## Instalacja i uruchomienie

### 1. Sklonuj/Pobierz projekt
Upewnij się, że masz wszystkie pliki projektu w katalogu.

### 2. Struktura projektu
```
SolarSystemSimulation/
├── pom.xml                    # Konfiguracja Maven
├── api-result.xml             # Dane planet z Wikipedii
├── README.md                  # Dokumentacja projektu
├── PATTERNS_EXAMPLES.md       # Przykłady wzorców
├── CLASS_DIAGRAM.puml         # Diagram klas
└── src/
    └── main/
        ├── java/
        │   ├── module-info.java
        │   └── com/example/solarsystemsimulation/
        │       ├── SolarSystemApplication.java
        │       ├── data/
        │       │   └── PlanetDataParser.java
        │       ├── model/
        │       │   └── CelestialBody.java
        │       ├── persistence/
        │       │   └── SimulationPersistence.java
        │       ├── physics/
        │       │   ├── Vector2D.java
        │       │   ├── PhysicsStrategy.java
        │       │   └── NewtonianPhysics.java
        │       ├── simulation/
        │       │   ├── Simulation.java
        │       │   ├── SimulationState.java
        │       │   ├── RunningState.java
        │       │   ├── StoppedState.java
        │       │   └── SimulationObserver.java
        │       └── ui/
        │           ├── SimulationController.java
        │           └── SimulationRenderer.java
        └── resources/
            └── com/example/solarsystemsimulation/
                └── simulation-view.fxml
```

### 3. Kompilacja projektu
```bash
mvn clean compile
```

### 4. Uruchomienie symulacji
```bash
mvn clean javafx:run
```

## Pierwsze kroki z aplikacją

### Interfejs użytkownika

Po uruchomieniu zobaczysz:
- **Canvas** (czarne tło) - obszar symulacji z planetami i orbitami
- **Panel sterowania** (na dole):
  - Przyciski: Start, Stop, Reset, Zapisz, Wczytaj
  - Slider prędkości: 0.5x, 1.0x, 4x, 16x

### Jak używać

1. **Uruchomienie symulacji**
   - Kliknij przycisk **Start**
   - Planety zaczną się poruszać wokół Słońca

2. **Zatrzymanie symulacji**
   - Kliknij przycisk **Stop**
   - Symulacja się zatrzyma

3. **Reset symulacji**
   - Kliknij przycisk **Reset**
   - Wszystkie planety wrócą do pozycji początkowych

4. **Zmiana prędkości**
   - Przesuń slider:
     - 0.5x - wolniej (pół prędkości)
     - 1.0x - normalna prędkość (Ziemia okrąża Słońce w ~10 sekund)
     - 4x - 4 razy szybciej
     - 16x - 16 razy szybciej

5. **Zapisywanie stanu**
   - Kliknij **Zapisz**
   - Wybierz lokalizację i nazwę pliku (.sim)
   - Stan symulacji zostanie zapisany

6. **Wczytywanie stanu**
   - Kliknij **Wczytaj**
   - Wybierz plik .sim
   - Stan symulacji zostanie przywrócony

## Co zobaczysz

- **Słońce** - żółte, w centrum
- **Planety wewnętrzne** (małe punkty):
  - Merkury (szary)
  - Wenus (pszeniczny)
  - Ziemia (niebieski)
  - Mars (pomarańczowo-czerwony)

- **Planety zewnętrzne** (większe punkty):
  - Jowisz (pomarańczowy)
  - Saturn (khaki)
  - Uran (jasnoniebieski)
  - Neptun (ciemnoniebieski)

- **Orbity** - szare okręgi pokazujące ścieżki orbit

- **Legenda** - w lewym górnym rogu, nazwy planet z kolorami

## Kalibracja fizyki

- **Jednostki odległości**: AU (astronomical units, 1 AU = odległość Ziemia-Słońce)
- **Jednostki czasu**: dni
- **Fizyka**: Prawo grawitacji Newtona (F = G·m₁·m₂/r²)
- **Metoda numeryczna**: Verlet (stabilna i dokładna)

## Rozwiązywanie problemów

### Problem: "Error: JavaFX runtime components are missing"
**Rozwiązanie**: Użyj komendy `mvn javafx:run` zamiast `mvn exec:java`

### Problem: Planety nie poruszają się płynnie
**Rozwiązanie**: 
- Sprawdź obciążenie CPU
- Spróbuj zmniejszyć prędkość symulacji (slider na 0.5x)

### Problem: Nie widać niektórych planet
**Rozwiązanie**: 
- Planety zewnętrzne są daleko - poczekaj lub zwiększ prędkość
- Sprawdź legendę - wszystkie planety są wymienione

### Problem: Błąd parsowania XML
**Rozwiązanie**: 
- Upewnij się, że plik `api-result.xml` jest w głównym katalogu projektu
- Jeśli go brakuje, aplikacja użyje domyślnych danych planet

## Testowanie wzorców

### Test wzorca Strategy
Zmień `NewtonianPhysics` na inną implementację w `SolarSystemApplication`:
```java
Simulation simulation = new Simulation(new MojaNowaFizyka());
```

### Test wzorca State
Obserwuj zachowanie przycisków Start/Stop - stan się zmienia automatycznie

### Test wzorca Observer
Dodaj nowego observera w kontrolerze:
```java
simulation.addObserver(new LoggingObserver());
```

### Test wzorca Memento
Użyj funkcji Reset - stan jest przywracany z memento

## Kolejne kroki

1. Przeczytaj **README.md** - pełna dokumentacja
2. Zobacz **PATTERNS_EXAMPLES.md** - szczegółowe przykłady wzorców
3. Przeanalizuj **CLASS_DIAGRAM.puml** - struktura klas
4. Eksperymentuj z kodem - dodaj nowe planety, zmień kolory, dostosuj prędkości

## Pomoc

Jeśli masz problemy:
1. Sprawdź czy wszystkie pliki są w odpowiednich katalogach
2. Upewnij się, że używasz Java 23
3. Sprawdź czy Maven jest zainstalowany: `mvn --version`
4. Sprawdź logi w konsoli

---

**Miłej zabawy z symulacją układu słonecznego!** 🌍🪐☀️
