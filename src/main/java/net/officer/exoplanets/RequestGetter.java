package net.officer.exoplanets;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    }
}
