package net.officer.exoplanets;

import com.google.gson.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestGetter {
    public static void main(String[] args) throws URISyntaxException, IOException {
        // Get planets
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the planet name: ");
        String planetName = userInput.nextLine();
        String starName = "";

        //averagePlanetInfo(planetName);
    }

    private static void averagePlanetInfo(String planetName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("src/main/resources/data/" + planetName + ".json");
        try (Reader reader = new FileReader(file)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            if (array != null) {
                String hostName = array.asList().getFirst().getAsJsonObject().get("hostname").getAsString();
                Map<String, List<Double>> valuesMap = Map.of("pl_orbsmax", getDoubles(array, "pl_orbsmax"), "pl_orbincl", getDoubles(array, "pl_orbincl"),
                        "st_mass", getDoubles(array, "st_mass"), "st_rad", getDoubles(array, "st_rad"), "ra", getDoubles(array, "ra"),
                        "dec", getDoubles(array, "dec"));

                JsonObject planet = new JsonObject();
                planet.addProperty("planet_name", planetName);
                planet.addProperty("hostname", hostName);
                for (var entry : valuesMap.entrySet()) {
                    double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
                    planet.addProperty(entry.getKey(), avg);
                }

                // Replace original file with averaged one
                try (Writer writer = new FileWriter("src/main/resources/data/" + planetName + ".json")) {
                    gson.toJson(planet, writer);
                    System.out.printf("Condensed %d planet entries", array.size());
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