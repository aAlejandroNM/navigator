package com.solvd.navigator.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class CliController {

    private final CommandRegistry registry;
    private final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger(CliController.class);

    public CliController(CommandRegistry registry) {
        this.registry = registry;
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                running = false;
                continue;
            }

            Command cmd = registry.get(input);

            if (cmd != null) {
                cmd.execute();
            } else {
                LOGGER.warn("Invalid option.");
            }
        }

        LOGGER.info("Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n=== NAVIGATOR MENU ===");

        registry.getAll().values().forEach(c ->
                System.out.printf("%s. %s\n", c.key(), c.name()));

        System.out.println("Q. Quit");
        System.out.println("Choose an option: ");
    }
}