# Historia zmian projektu

## Wersja 2.0 - Rozszerzona legenda z danymi planet

### Zmiany:
1. **Rozszerzona legenda w GUI**:
   - Dodano wyświetlanie aktualnej pozycji planety (X, Y) w AU
   - Dodano wektor prędkości (vX, vY) oraz wartość prędkości
   - Dodano odległość od Słońca w czasie rzeczywistym
   - Legenda ma teraz semi-transparentne tło dla lepszej czytelności

2. **Zwiększone okno aplikacji**:
   - Szerokość: 1200 → 1400 pikseli
   - Wysokość: 800 → 850 pikseli
   - Canvas: 1200x700 → 1400x750 pikseli
   - Więcej miejsca na szczegółowe informacje

3. **Ulepszona wizualizacja**:
   - Każda planeta ma teraz osobną sekcję w legendzie
   - Dane aktualizują się w czasie rzeczywistym podczas symulacji
   - Czytelne formatowanie liczb (3-4 miejsca po przecinku)

### Przykład wyświetlanych danych:
```
Ziemia 🔵
  Pozycja: (1.000, 0.000) AU
  Prędkość: 0.0172 AU/dzień (0.000, 0.017)
  Odległość od Słońca: 1.000 AU
```

### Pliki zmienione:
- `SimulationRenderer.java` - rozszerzona metoda `drawLegend()`
- `simulation-view.fxml` - zwiększone wymiary okna
- `SolarSystemApplication.java` - zaktualizowane wymiary sceny

---

## Wersja 1.1 - Poprawka prędkości orbitalnych

### Problem:
Planety spadały na Słońce zamiast krążyć po orbitach.

### Rozwiązanie:
1. Poprawiono wzór obliczania prędkości orbitalnej:
   - Stary: `v = sqrt(1.0 / r) * 0.01720209895`
   - Nowy: `v = sqrt(G * M_słońca / r)`

2. Użyto poprawnych stałych:
   - `G = 2.95912208286e-4` AU³/(M☉·day²)
   - Uwzględniono rzeczywistą masę Słońca

3. Upewniono się, że Słońce ma ustawioną flagę `isSun = true`

### Pliki zmienione:
- `PlanetDataParser.java` - metoda `createPlanet()` i `createSun()`

---

## Wersja 1.0 - Pierwotna wersja

### Funkcjonalności:
- Symulacja fizyczna 8 planet + Słońce
- Mechanika Newtona (grawitacja)
- GUI z kontrolkami: Start, Stop, Reset, Save, Load
- Slider prędkości: 0.5x, 1.0x, 4x, 16x
- Parser danych z XML (Wikipedia)
- Wzorce projektowe: Strategy, State, Observer, Memento, Adapter, Decorator, Iterator
- Zasady SOLID

### Struktura:
- 14 klas Java w 6 pakietach
- 1 plik FXML (GUI)
- Pełna dokumentacja (README, QUICK_START, PATTERNS_EXAMPLES)
- Diagram klas (PlantUML)
