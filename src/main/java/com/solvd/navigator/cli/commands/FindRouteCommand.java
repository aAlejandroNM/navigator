package com.solvd.navigator.cli.commands;

import com.solvd.navigator.cli.Command;
import com.solvd.navigator.dto.PathResult;
import com.solvd.navigator.controller.NavigationController;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class FindRouteCommand implements Command {

    private final NavigationController controller;
    private final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger(FindRouteCommand.class);

    public FindRouteCommand(NavigationController controller) {
        this.controller = controller;
    }

    @Override
    public String name() { return "Find shortest route"; }

    @Override
    public String key() { return "5"; }

    @Override
    public void execute() {
        LOGGER.info("Source: ");
        String src = scanner.nextLine();

        LOGGER.info("Destination: ");
        String dst = scanner.nextLine();

        PathResult result = controller.findPath(src, dst);

        LOGGER.info("\n--- Shortest Path Result ---");
        LOGGER.info(result);
    }
}