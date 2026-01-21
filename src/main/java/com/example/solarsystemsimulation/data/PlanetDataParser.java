package com.example.solarsystemsimulation.data;

import com.example.solarsystemsimulation.model.CelestialBody;
import com.example.solarsystemsimulation.physics.Vector2D;
import javafx.scene.paint.Color;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;
import java.util.regex.*;

/**
 * Parser danych planet z pliku XML (wzorzec Adapter)
 */
public class PlanetDataParser {
    private static final double EARTH_DIAMETER_KM = 12756.0;
    private static final double EARTH_MASS_KG = 5.972e24;
    private static final double SUN_MASS_KG = 1.989e30;
    
    // Rzeczywiste odległości od Słońca w AU
    private static final Map<String, Double> ORBITAL_DISTANCES = new HashMap<>();
    static {
        ORBITAL_DISTANCES.put("Merkury", 0.39);
        ORBITAL_DISTANCES.put("Wenus", 0.72);
        ORBITAL_DISTANCES.put("Ziemia", 1.0);
        ORBITAL_DISTANCES.put("Mars", 1.52);
        ORBITAL_DISTANCES.put("Jowisz", 5.2);
        ORBITAL_DISTANCES.put("Saturn", 9.54);
        ORBITAL_DISTANCES.put("Uran", 19.19);
        ORBITAL_DISTANCES.put("Neptun", 30.07);
    }
    
    // Kolory planet
    private static final Map<String, Color> PLANET_COLORS = new HashMap<>();
    static {
        PLANET_COLORS.put("Merkury", Color.GRAY);
        PLANET_COLORS.put("Wenus", Color.WHEAT);
        PLANET_COLORS.put("Ziemia", Color.BLUE);
        PLANET_COLORS.put("Mars", Color.ORANGERED);
        PLANET_COLORS.put("Jowisz", Color.ORANGE);
        PLANET_COLORS.put("Saturn", Color.KHAKI);
        PLANET_COLORS.put("Uran", Color.LIGHTBLUE);
        PLANET_COLORS.put("Neptun", Color.DARKBLUE);
    }

    /**
     * Parsuje plik XML i zwraca listę ciał niebieskich
     */
    public static List<CelestialBody> parsePlanetsFromXML(String xmlPath) {
        List<CelestialBody> bodies = new ArrayList<>();
        
        try {
            // Dodaj Słońce jako pierwszy obiekt
            CelestialBody sun = createSun();
            bodies.add(sun);
            
            // Parsuj XML
            File xmlFile = new File(xmlPath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            
            String content = doc.getElementsByTagName("text").item(0).getTextContent();
            
            // Parsuj dane planet z tabeli HTML w XML
            bodies.addAll(parsePlanetsFromTable(content));
            
        } catch (Exception e) {
            System.err.println("Błąd parsowania XML: " + e.getMessage());
            e.printStackTrace();
            // Zwróć domyślne planety
            return getDefaultPlanets();
        }
        
        return bodies;
    }
    
    private static List<CelestialBody> parsePlanetsFromTable(String htmlContent) {
        List<CelestialBody> planets = new ArrayList<>();
        
        // Regex do wyciągnięcia wierszy tabeli z planetami
        Pattern rowPattern = Pattern.compile("<tr[^>]*>.*?</tr>", Pattern.DOTALL);
        Matcher rowMatcher = rowPattern.matcher(htmlContent);
        
        while (rowMatcher.find()) {
            String row = rowMatcher.group();
            
            // Szukaj nazwy planety i średnicy
            if (row.contains("planeta</td>") && !row.contains("karłowata")) {
                String planetName = extractPlanetName(row);
                double diameter = extractDiameter(row);
                
                if (planetName != null && diameter > 0 && ORBITAL_DISTANCES.containsKey(planetName)) {
                    CelestialBody planet = createPlanet(planetName, diameter);
                    if (planet != null) {
                        planets.add(planet);
                    }
                }
            }
        }
        
        return planets;
    }
    
    private static String extractPlanetName(String row) {
        Pattern namePattern = Pattern.compile("title=\"([^\"]+)\"[^>]*>([^<]+)</a>");
        Matcher nameMatcher = namePattern.matcher(row);
        
        if (nameMatcher.find()) {
            String name = nameMatcher.group(2).trim();
            // Normalizuj nazwę
            if (PLANET_COLORS.containsKey(name)) {
                return name;
            }
        }
        return null;
    }
    
    private static double extractDiameter(String row) {
        // Szukamy średnicy (pierwsza liczba w wierszu po "planeta")
        Pattern diameterPattern = Pattern.compile("<td>(\\d+)</td>");
        Matcher diameterMatcher = diameterPattern.matcher(row);
        
        if (diameterMatcher.find()) {
            try {
                return Double.parseDouble(diameterMatcher.group(1));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    private static CelestialBody createSun() {
        CelestialBody sun = new CelestialBody(
            "Słońce",
            SUN_MASS_KG / EARTH_MASS_KG, // Masa względem Ziemi
            696000, // promień w km
            Color.YELLOW,
            Vector2D.zero(),
            Vector2D.zero()
        );
        sun.setIsSun(true);
        return sun;
    }
    
    private static CelestialBody createPlanet(String name, double diameterKm) {
        double orbitalDistance = ORBITAL_DISTANCES.get(name);
        Color color = PLANET_COLORS.getOrDefault(name, Color.WHITE);
        
        // Oblicz masę na podstawie średnicy (przybliżenie)
        double massRelativeToEarth = Math.pow(diameterKm / EARTH_DIAMETER_KM, 3);
        
        // Oblicz prędkość orbitalną
        // v = sqrt(G * M_sun / r)
        // G w jednostkach AU³/(M☉·day²) = 2.95912208286e-4
        // M_sun = 1 (w jednostkach masy Słońca)
        // Dla jednostek AU/day:
        final double G_AU = 2.95912208286e-4;
        final double M_SUN = SUN_MASS_KG / EARTH_MASS_KG; // Masa Słońca względem Ziemi
        
        // v = sqrt(G * M / r) - prędkość orbitalna w AU/day
        double orbitalVelocity = Math.sqrt(G_AU * M_SUN / orbitalDistance);
        
        CelestialBody planet = new CelestialBody(
            name,
            massRelativeToEarth,
            diameterKm / 2.0, // promień
            color,
            new Vector2D(orbitalDistance, 0), // początkowa pozycja na osi X
            new Vector2D(0, orbitalVelocity) // początkowa prędkość prostopadła (Y)
        );
        
        return planet;
    }
    
    /**
     * Zwraca domyślny zestaw planet (fallback)
     */
    public static List<CelestialBody> getDefaultPlanets() {
        List<CelestialBody> bodies = new ArrayList<>();
        
        // Słońce
        CelestialBody sun = createSun();
        sun.setIsSun(true);
        bodies.add(sun);
        
        // Planety wewnętrzne
        bodies.add(createPlanet("Merkury", 4878));
        bodies.add(createPlanet("Wenus", 12104));
        bodies.add(createPlanet("Ziemia", 12756));
        bodies.add(createPlanet("Mars", 6786));
        
        // Planety zewnętrzne (gazowe olbrzymy)
        bodies.add(createPlanet("Jowisz", 142700));
        bodies.add(createPlanet("Saturn", 120400));
        bodies.add(createPlanet("Uran", 51100));
        bodies.add(createPlanet("Neptun", 49500));
        
        return bodies;
    }
}
