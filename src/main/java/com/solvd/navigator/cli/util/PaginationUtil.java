package com.solvd.navigator.cli.util;

import com.solvd.navigator.model.Location;

import java.util.List;
import java.util.Scanner;

public class PaginationUtil {

    private static final int PAGE_SIZE = 10;

    public static void paginateLocations(List<Location> locations) {
        Scanner scanner = new Scanner(System.in);

        int total = locations.size();
        int pages = (int) Math.ceil(total / (double) PAGE_SIZE);

        for (int page = 0; page < pages; page++) {
            int start = page * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, total);

            System.out.printf("\n--- Locations (%d–%d of %d) ---\n",
                    start + 1, end, total);

            for (int i = start; i < end; i++) {
                Location loc = locations.get(i);
                System.out.printf("[%d] %s (%s)\n",
                        loc.getId(), loc.getName(), loc.getType());
            }

            if (page < pages - 1) {
                System.out.print("Press ENTER for next page or 'q' to stop: ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) break;
            }
        }
    }
}