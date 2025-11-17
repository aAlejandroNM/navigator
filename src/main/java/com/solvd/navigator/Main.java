package com.solvd.navigator;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Route;
import com.solvd.navigator.model.Location;
import com.solvd.navigator.model.RouteLocation;

import com.solvd.navigator.dao.mysql.impl.EdgeDao;
import com.solvd.navigator.dao.mysql.impl.RouteDao;
import com.solvd.navigator.dao.mysql.impl.LocationDao;
import com.solvd.navigator.dao.mysql.impl.RouteLocationDao;

import com.solvd.navigator.dao.mysql.interfaces.IEdgeDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteLocationDao;

import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.controller.NavigationController;

import com.solvd.navigator.service.NavigationService;
import com.solvd.navigator.service.RouteService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        // -----------------------------
        // 1. Initialize DAO Layer
        // -----------------------------
        ILocationDao locationDao = new LocationDao();
        IEdgeDao edgeDao = new EdgeDao(locationDao);
        IRouteDao routeDao = new RouteDao();
        IRouteLocationDao routeLocationDao = new RouteLocationDao(locationDao);

        // -----------------------------
        // 2. Initialize Service Layer
        // -----------------------------
        NavigationService navigationService =
                NavigationService.fromDaos(locationDao, edgeDao, routeDao, routeLocationDao);

        // -----------------------------
        // 3. Initialize Controller
        // -----------------------------
        NavigationController controller = new NavigationController(navigationService);

        // -----------------------------
        // 4. CLI user input
        // -----------------------------
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== NAVIGATOR SYSTEM ===");
        System.out.println("All locations loaded:");
        controller.getAllLocations()
                .forEach(loc -> System.out.println("- " + loc.getName()));

        System.out.print("\nEnter source location: ");
        String source = scanner.nextLine();

        System.out.print("Enter target location: ");
        String target = scanner.nextLine();

        // -----------------------------
        // 5. Call Controller
        // -----------------------------
        try {
            PathResult result = controller.findPath(source, target);
            System.out.println("\n=== RESULT ===");
            System.out.println(result.toString());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    private static List<Location> createExampleLocations() {
        List<Location> locations = new ArrayList<>();

        locations.add(new Location.Builder()
                .withId(1L)
                .withName("A")
                .withX(0.0)
                .withY(0.0)
                .withType("Punto")
                .withDescription("Ubicación A")
                .build());

        locations.add(new Location.Builder()
                .withId(2L)
                .withName("B")
                .withX(1.0)
                .withY(0.0)
                .withType("Punto")
                .withDescription("Ubicación B")
                .build());

        locations.add(new Location.Builder()
                .withId(3L)
                .withName("C")
                .withX(2.0)
                .withY(0.0)
                .withType("Punto")
                .withDescription("Ubicación C")
                .build());

        locations.add(new Location.Builder()
                .withId(4L)
                .withName("Z")
                .withX(3.0)
                .withY(0.0)
                .withType("Punto")
                .withDescription("Ubicación Z")
                .build());

        return locations;
    }

    private static List<Route> createExampleRoutes(List<Location> locations) {
        List<Route> routes = new ArrayList<>();

        // Ruta principal: A -> B -> C -> Z
        Route mainRoute = new Route.Builder()
                .withId(1L)
                .withName("Avenida Principal")
                .withDescription("Ruta principal que conecta A, B, C y Z")
                .withLocations(createRouteLocations(locations, new String[]{"A", "B", "C", "Z"}))
                .build();

        routes.add(mainRoute);

        return routes;
    }

    private static List<RouteLocation> createRouteLocations(
            List<Location> allLocations, String[] locationNames) {
        List<RouteLocation> routeLocations = new ArrayList<>();
        Route route = new Route.Builder().withId(1L).build();

        for (int index = 0; index < locationNames.length; index++) {
            Location location = findLocationByName(allLocations, locationNames[index]);
            if (location != null) {
                RouteLocation routeLocation = new RouteLocation.Builder()
                        .withRoute(route)
                        .withLocation(location)
                        .withPosition((long) index)
                        .build();
                routeLocations.add(routeLocation);
            }
        }

        return routeLocations;
    }

    private static Location findLocationByName(List<Location> locations, String name) {
        return locations.stream()
                .filter(location -> name.equals(location.getName()))
                .findFirst()
                .orElse(null);
    }

    private static List<Edge> createAdditionalEdges(List<Location> locations) {
        List<Edge> edges = new ArrayList<>();

        Location locationA = findLocationByName(locations, "A");
        Location locationC = findLocationByName(locations, "C");

        if (locationA != null && locationC != null) {
            // Direct connection A -> C (shorter than A -> B -> C)
            double directDistance = RouteService.calculateEuclideanDistance(locationA, locationC);
            Edge directEdge = new Edge.Builder()
                    .withFrom(locationA)
                    .withTo(locationC)
                    .withWeight(directDistance)
                    .withDirected(false)
                    .withName("Conexión directa A-C")
                    .withActive(true)
                    .build();
            edges.add(directEdge);
        }

        return edges;
    }


    private static void runNavigationInterface(
            NavigationController navigationController, List<Location> locations) {
        Scanner scanner = new Scanner(System.in);

        LOGGER.info("\n=== Interfaz de Navegación ===");
        LOGGER.info("Ubicaciones disponibles:");
        locations.forEach(location ->
            LOGGER.info("  - {}", location.getName())
        );

        try {
            LOGGER.info("\nIngrese el nombre de la ubicación origen: ");
            String sourceLocationName = scanner.nextLine().trim();

            LOGGER.info("Ingrese el nombre de la ubicación destino: ");
            String targetLocationName = scanner.nextLine().trim();

            LOGGER.info("\n[Llamada simulada a endpoint: GET /api/navigation/path]");
            PathResult pathResult = navigationController.findPath(
                    sourceLocationName,
                    targetLocationName
            );

            LOGGER.info("\n=== Resultado de la Búsqueda ===");
            LOGGER.info(pathResult.toString());

        } catch (IllegalArgumentException exception) {
            LOGGER.error("Error: {}", exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error("Error inesperado: {}", exception.getMessage());
            exception.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}