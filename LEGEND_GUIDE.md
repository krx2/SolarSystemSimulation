# Nowa Legenda - Szczegółowe Dane Planet

## Wygląd legendy

Legenda jest teraz znacznie bardziej informacyjna i wyświetla się w lewym górnym rogu okna symulacji na semi-transparentnym tle.

### Przykład wyświetlanych informacji:

```
┌─────────────────────────────────────────────────┐
│ Słońce 🟡                                       │
│   Pozycja: (0.000, 0.000) AU                   │
│   Prędkość: 0.0000 AU/dzień (0.000, 0.000)     │
│   Odległość od Słońca: 0.000 AU (centrum)      │
│                                                 │
│ Merkury ⚪                                      │
│   Pozycja: (0.387, 0.042) AU                   │
│   Prędkość: 0.0826 AU/dzień (0.009, 0.082)     │
│   Odległość od Słońca: 0.389 AU                │
│                                                 │
│ Wenus 🟨                                        │
│   Pozycja: (0.715, 0.089) AU                   │
│   Prędkość: 0.0610 AU/dzień (0.007, 0.061)     │
│   Odległość od Słońca: 0.721 AU                │
│                                                 │
│ Ziemia 🔵                                       │
│   Pozycja: (0.998, 0.067) AU                   │
│   Prędkość: 0.0517 AU/dzień (0.003, 0.052)     │
│   Odległość od Słońca: 1.000 AU                │
│                                                 │
│ Mars 🔴                                         │
│   Pozycja: (1.516, 0.078) AU                   │
│   Prędkość: 0.0419 AU/dzień (0.002, 0.042)     │
│   Odległość od Słońca: 1.518 AU                │
│                                                 │
│ Jowisz 🟠                                       │
│   Pozycja: (5.192, 0.223) AU                   │
│   Prędkość: 0.0227 AU/dzień (0.001, 0.023)     │
│   Odległość od Słońca: 5.197 AU                │
│                                                 │
│ [... pozostałe planety ...]                    │
└─────────────────────────────────────────────────┘
```

## Co pokazuje każda linia:

### 1. Nazwa planety + kolor
- **Nazwa**: Oficjalna nazwa ciała niebieskiego
- **Kolorowe kółko**: Wizualna identyfikacja planety (taki sam kolor jak na canvas)

### 2. Pozycja (X, Y) w AU
- **Format**: `(X, Y)` gdzie X i Y to współrzędne w jednostkach astronomicznych
- **AU (Astronomical Unit)**: 1 AU = ~150 mln km (odległość Ziemia-Słońce)
- **Wartości**: 
  - Aktualizują się w czasie rzeczywistym
  - Dokładność: 3 miejsca po przecinku
  - Centrum układu (0, 0) to Słońce

### 3. Wektor prędkości
- **Prędkość całkowita**: Moduł wektora prędkości w AU/dzień
- **Format**: `wielkość AU/dzień (vX, vY)`
- **Komponenty**:
  - `vX`: prędkość w osi X
  - `vY`: prędkość w osi Y
- **Dokładność**: 4 miejsca po przecinku dla całkowitej prędkości, 3 dla komponentów

### 4. Odległość od Słońca
- **Obliczana dynamicznie**: `sqrt(X² + Y²)`
- **Dokładność**: 3 miejsca po przecinku
- **Specjalny przypadek**: Dla Słońca wyświetla "(centrum)"

## Dynamiczne aktualizacje

Wszystkie dane aktualizują się **w czasie rzeczywistym** podczas działania symulacji:

- **60 FPS**: Dane odświeżają się z każdą klatką animacji
- **Płynne zmiany**: Możesz obserwować jak planety poruszają się po orbitach
- **Przyspieszenie**: Dane zmieniają się szybciej przy wyższych prędkościach (4x, 16x)

## Zastosowania

### Edukacyjne:
- Obserwacja orbit planet w czasie rzeczywistym
- Analiza wektorów prędkości
- Weryfikacja praw Keplera (planety bliżej Słońca poruszają się szybciej)

### Debugowanie:
- Sprawdzenie poprawności fizyki symulacji
- Weryfikacja stabilności orbit
- Obserwacja zachowania przy różnych prędkościach

### Eksperymentowanie:
- Porównywanie prędkości różnych planet
- Obserwacja zmian odległości (orbity nie są idealnie okrągłe w symulacji)
- Analiza trajektorii

## Przykładowe obserwacje:

### Prędkości orbitalne (AU/dzień):
- **Merkury**: ~0.0826 (najszybszy)
- **Wenus**: ~0.0610
- **Ziemia**: ~0.0517
- **Mars**: ~0.0419
- **Jowisz**: ~0.0227
- **Saturn**: ~0.0168
- **Uran**: ~0.0118
- **Neptun**: ~0.0094 (najwolniejszy)

### Jak interpretować dane:

**Przykład - Ziemia:**
```
Pozycja: (0.998, 0.067) AU
Prędkość: 0.0517 AU/dzień (0.003, 0.052)
Odległość od Słońca: 1.000 AU
```

**Interpretacja:**
1. Ziemia znajduje się prawie dokładnie w odległości 1 AU od Słońca ✅
2. Porusza się głównie w kierunku Y (0.052) z niewielką składową X (0.003)
3. Całkowita prędkość: ~0.0517 AU/dzień = ~30 km/s (zgodne z rzeczywistością!) ✅
4. Orbita jest prawie okrągła (odległość oscyluje wokół 1.000 AU)

## Wymiary okna

Aby pomieścić rozszerzoną legendę:
- **Szerokość**: 1400px (poprzednio 1200px)
- **Wysokość**: 850px (poprzednio 800px)
- **Canvas**: 1400x750px (poprzednio 1200x700px)

## Czytelność

- **Tło**: Semi-transparentne czarne (rgba(0, 0, 0, 0.7))
- **Tekst**: Biały, czytelny na czarnym tle kosmosu
- **Czcionka nazw**: Arial Bold 12px
- **Czcionka danych**: Arial 10px
- **Odstępy**: 65px między planetami dla lepszej separacji
