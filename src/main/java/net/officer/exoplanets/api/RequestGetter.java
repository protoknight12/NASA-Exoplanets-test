package net.officer.exoplanets.api;

import com.google.gson.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestGetter {
    public static void main(String[] args) throws URISyntaxException, IOException {
        // Get planets and stars
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the planet name: ");
        String planetName = userInput.nextLine();

        getRequestPlanet(planetName);
        averagePlanetInfo(planetName);

        try(Reader reader = new FileReader("src/main/resources/data/planets/" + planetName + ".json")) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            if(!obj.isEmpty()) {
                String hostName = obj.get("hostname").getAsString();
                getRequestStar(hostName);
                averageStarInfo(hostName);
            }
        }
    }

    private static void averagePlanetInfo(String planetName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        File file = new File("src/main/resources/data/planets/" + planetName + ".json");
        try (Reader reader = new FileReader(file)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            if (array != null) {
                String hostName = array.asList().getFirst().getAsJsonObject().get("hostname").getAsString();
                Map<String, List<Double>> valuesMap = Map.of("pl_orbital_sm_axis", getDoubles(array, "pl_orbsmax"), "pl_orbital_incl", getDoubles(array, "pl_orbincl"),
                        "pl_radius", getDoubles(array, "pl_rade"), "pl_mass", getDoubles(array, "pl_bmasse"), "pl_density", getDoubles(array, "pl_dens"),
                        "pl_temperature", getDoubles(array, "pl_eqt"), "pl_impact_parameter", getDoubles(array, "pl_imppar"), "pl_rad_vel_ampl", getDoubles(array, "pl_rvamp"));

                JsonObject planet = new JsonObject();
                planet.addProperty("pl_name", planetName);
                planet.addProperty("hostname", hostName);
                for (var entry : valuesMap.entrySet()) {
                    double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
                    if(Double.isNaN(avg)) {
                        planet.add(entry.getKey(), JsonNull.INSTANCE);
                    } else {
                        planet.addProperty(entry.getKey(), avg);
                    }
                }

                // Replace original file with averaged one
                try (Writer writer = new FileWriter("src/main/resources/data/planets/" + planetName + ".json")) {
                    gson.toJson(planet, writer);
                    System.out.printf("Condensed %d planet entries", array.size());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void averageStarInfo(String starName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        File file = new File("src/main/resources/data/stars/" + starName + ".json");
        try (Reader reader = new FileReader(file)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            if (array != null) {
                String hostName = array.asList().getFirst().getAsJsonObject().get("hostname").getAsString();
                Map<String, List<Double>> valuesMap = Map.of("right_ascension", getDoubles(array, "ra"), "declination", getDoubles(array, "dec"),
                        "st_radius", getDoubles(array, "st_rad"), "st_mass", getDoubles(array, "st_mass"), "st_age", getDoubles(array, "st_age"),
                        "st_density", getDoubles(array, "st_dens"), "st_rot_period", getDoubles(array, "st_rotp"), "st_temperature", getDoubles(array, "st_teff"));

                JsonObject planet = new JsonObject();
                planet.addProperty("hostname", hostName);
                for (var entry : valuesMap.entrySet()) {
                    double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
                    if(Double.isNaN(avg)) {
                        planet.add(entry.getKey(), JsonNull.INSTANCE);
                    } else {
                        planet.addProperty(entry.getKey(), avg);
                    }
                }

                try (Writer writer = new FileWriter("src/main/resources/data/stars/" + starName + ".json")) {
                    gson.toJson(planet, writer);
                    System.out.printf("\nCondensed %d star entries", array.size());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Double> getDoubles(JsonArray array, String key) {
        return array.asList().stream().map(JsonElement::getAsJsonObject).map(obj -> obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsDouble() : null).filter(Objects::nonNull).toList();
    }

    private static void getRequestPlanet(String name) throws IOException, URISyntaxException {
        String planetUrl = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=SELECT+pl_name,+hostname,+pl_orbsmax,+pl_orbincl,+pl_rade,+pl_bmasse,+pl_dens,+pl_eqt,+pl_imppar,+pl_rvamp+FROM+ps+WHERE+pl_name=%27" + URLEncoder.encode(name, StandardCharsets.UTF_8) + "%27&format=json";
        URI request = new URI(planetUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(request.toURL().openStream()));
        String inputLine;
        File file = new File("src/main/resources/data/planets/" + name + ".json");
        StringBuilder contents = new StringBuilder();

        // Extract the info
        while ((inputLine = in.readLine()) != null) {
            contents.append(inputLine);
            try (Writer writer = new FileWriter(file)) {
                writer.write(String.valueOf(contents));
            }
        }
        in.close();
    }

    private static void getRequestStar(String name) throws IOException, URISyntaxException {
        String starUrl = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=SELECT+hostname,+ra,+dec,+st_rad,+st_mass,+st_age,+st_dens,+st_rotp,+st_teff+FROM+stellarhosts+WHERE+hostname=%27" + URLEncoder.encode(name, StandardCharsets.UTF_8) + "%27&format=json";
        URI request = new URI(starUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(request.toURL().openStream()));
        String inputLine;
        File file = new File("src/main/resources/data/stars/" + name + ".json");
        StringBuilder contents = new StringBuilder();

        // Extract the info
        while ((inputLine = in.readLine()) != null) {
            contents.append(inputLine);
            try (Writer writer = new FileWriter(file)) {
                writer.write(String.valueOf(contents));
            }
        }
        in.close();
    }
}