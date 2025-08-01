package net.officer.exoplanets;

import com.google.gson.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RequestGetter {


    public static void main(String[] args) throws URISyntaxException, IOException {
        //Get planets
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the planet name: ");
        String planetName = userInput.nextLine();
        //Format the url and prepare for extraction
        String url = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=SELECT+pl_name,+hostname,+pl_orbsmax,+pl_orbincl,+st_mass,+st_rad,+ra,+dec+FROM+ps+WHERE+pl_name=%27" + URLEncoder.encode(planetName, StandardCharsets.UTF_8) + "%27&format=json";
        URI request = new URI(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(request.toURL().openStream()));
        String inputLine;
        File file = new File("src/main/resources/data/" + planetName + ".json");
        StringBuilder contents = new StringBuilder();
        //Extract the info
        while ((inputLine = in.readLine()) != null) {
            contents.append(inputLine);
            try (Writer writer = new FileWriter(file)) {
                writer.write(String.valueOf(contents));
            }
        }
        in.close();
        averagePlanetInfo(planetName);
    }

    private static void averagePlanetInfo(String planetName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("src/main/resources/data/" + planetName + ".json");
        try (Reader reader = new FileReader(file)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            if (array != null) {
                String hostName = array.asList().getFirst().getAsJsonObject().get("hostname").getAsString();
                List<Double> orbsMax = new ArrayList<>();
                List<Double> orbIncl = new ArrayList<>();
                List<Double> mass = new ArrayList<>();
                List<Double> radius = new ArrayList<>();
                List<Double> rightAsc = new ArrayList<>();
                List<Double> declination = new ArrayList<>();

                for (JsonElement elem : array) {
                    JsonObject jsonObj = elem.getAsJsonObject();
                    double orbsMax1 = getDoubleOrDefault(jsonObj,"pl_orbsmax");
                    double orbIncl1 = getDoubleOrDefault(jsonObj,"pl_orbincl");
                    double mass1 = getDoubleOrDefault(jsonObj,"st_mass");
                    double radius1 = getDoubleOrDefault(jsonObj,"st_rad");
                    double rightAsc1 = getDoubleOrDefault(jsonObj,"ra");
                    double declination1 = getDoubleOrDefault(jsonObj,"dec");

                    orbsMax.add(orbsMax1);
                    orbIncl.add(orbIncl1);
                    mass.add(mass1);
                    radius.add(radius1);
                    rightAsc.add(rightAsc1);
                    declination.add(declination1);
                }
                JsonArray avrgStats = new JsonArray();
                JsonObject planet = new JsonObject();
                planet.addProperty("pl_name", planetName);
                planet.addProperty("hostname", hostName);
                planet.addProperty("pl_orbsmax", orbsMax.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN));
                planet.addProperty("pl_orbincl", orbIncl.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN));
                planet.addProperty("st_mass", mass.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN));
                planet.addProperty("st_rad", radius.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN));
                planet.addProperty("ra", rightAsc.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN));
                planet.addProperty("dec", declination.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN));

                avrgStats.add(planet);
                try (Writer writer = new FileWriter("src/main/resources/data/" + planetName + "(avrg)" + ".json")) {
                    gson.toJson(avrgStats, writer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static double getDoubleOrDefault(JsonObject object, String key) {
        return object.has(key) && !object.get(key).isJsonNull() ? object.get(key).getAsDouble() : 1d;
    }
}
