package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import javax.servlet.http.HttpServletResponse;


/** Returns bus stop data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}] */
@WebServlet("/bus-stops")
public class MapServlet extends HttpServlet {
  JsonArray busStops;

  @Override
  public void init() {
    busStops = new JsonArray();
    Gson gson = new Gson();
    Scanner scanner =
        new Scanner(getServletContext().getResourceAsStream("/WEB-INF/bus_stops.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      String neighborhood = cells[0];
      double lat = Double.parseDouble(cells[1]);
      double lng = Double.parseDouble(cells[2]);

      busStops.add(gson.toJsonTree(new BusStop(neighborhood, lat, lng)));
    }
    scanner.close();
  }

  /**
   * Responds with a JSON representation of {@link BusStop} data for a specific user. Responds with
   * printing out the bus stop information.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(busStops.toString());
  }

  /** A single bus stop. */
  private static class BusStop {
    String neighborhood;
    double lat;
    double lng;

    /**
     * Construct a bus stop with the neighborhood where the bus stop is located and latitude and
     * longitude of it.
     */
    private BusStop(String neighborhood, double lat, double lng) {
      this.neighborhood = neighborhood;
      this.lat = lat;
      this.lng = lng;
    }
  }
}
