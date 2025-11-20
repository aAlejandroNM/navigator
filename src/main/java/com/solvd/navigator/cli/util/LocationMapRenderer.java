package com.solvd.navigator.cli.util;

import com.solvd.navigator.model.Edge;
import com.solvd.navigator.model.Location;

import java.util.*;

public class LocationMapRenderer {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 25;

    record Point(int x, int y) {}

    public static void renderGraph(List<Location> locations, List<Edge> edges) {

        char[][] canvas = new char[HEIGHT][WIDTH];

        for (int y = 0; y < HEIGHT; y++) {
            Arrays.fill(canvas[y], '·');
        }

        double minX = locations.stream().mapToDouble(Location::getX).min().orElse(0);
        double maxX = locations.stream().mapToDouble(Location::getX).max().orElse(1);
        double minY = locations.stream().mapToDouble(Location::getY).min().orElse(0);
        double maxY = locations.stream().mapToDouble(Location::getY).max().orElse(1);

        Map<Long, Point> pointMap = new HashMap<>();
        Map<String, String> legend = new LinkedHashMap<>();

        for (Location loc : locations) {

            int px = (int) ((loc.getX() - minX) / (maxX - minX) * (WIDTH - 2));
            int py = (int) ((loc.getY() - minY) / (maxY - minY) * (HEIGHT - 1));

            py = HEIGHT - 1 - py;

            pointMap.put(loc.getId(), new Point(px, py));

            String label = computeLabel(loc.getName());
            legend.put(label, loc.getName());

            placeLabel(canvas, px, py, label);
        }

        for (Edge edge : edges) {
            Location from = edge.getFrom();
            Location to = edge.getTo();

            int x1 = (int) ((from.getX() - minX) / (maxX - minX) * (WIDTH - 1));
            int y1 = HEIGHT - 1 - (int) ((from.getY() - minY) / (maxY - minY) * (HEIGHT - 1));

            int x2 = (int) ((to.getX() - minX) / (maxX - minX) * (WIDTH - 1));
            int y2 = HEIGHT - 1 - (int) ((to.getY() - minY) / (maxY - minY) * (HEIGHT - 1));

            drawLine(canvas, x1, y1, x2, y2);
        }

        System.out.println("\n=== GRAPH MAP (XY PLANE) ===\n");
        for (char[] row : canvas) {
            System.out.println(new String(row));
        }

        System.out.println("\nLegend:");
        legend.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    private static String computeLabel(String name) {
        String[] parts = name.split(" ");
        if (parts.length == 1) {
            return name.substring(0, Math.min(2, name.length())).toUpperCase();
        }
        return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
    }

    private static void placeLabel(char[][] canvas, int x, int y, String label) {
        if (x >= 0 && x < WIDTH - 1 && y >= 0 && y < HEIGHT) {
            canvas[y][x] = label.charAt(0);
            canvas[y][x + 1] = label.charAt(1);
        }
    }

    private static void drawLine(char[][] canvas, int x0, int y0, int x1, int y1) {
        int dx = Math.abs(x1 - x0);
        int dy = -Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;

        while (true) {

            char ch = chooseLineChar(x0, y0, x1, y1);

            if (canvas[y0][x0] == '·' || canvas[y0][x0] == '─' || canvas[y0][x0] == '│' || canvas[y0][x0] == '+') {
                canvas[y0][x0] = ch;
            }

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;

            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    private static char chooseLineChar(int x0, int y0, int x1, int y1) {
        if (y0 == y1) return '─';
        if (x0 == x1) return '│';
        return '+';
    }
}
