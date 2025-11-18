package com.solvd.navigator;

import com.solvd.navigator.controller.CliController;
import com.solvd.navigator.dao.mysql.impl.EdgeDao;
import com.solvd.navigator.dao.mysql.impl.RouteDao;
import com.solvd.navigator.dao.mysql.impl.LocationDao;
import com.solvd.navigator.dao.mysql.impl.RouteLocationDao;

import com.solvd.navigator.dao.mysql.interfaces.IEdgeDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteDao;
import com.solvd.navigator.dao.mysql.interfaces.ILocationDao;
import com.solvd.navigator.dao.mysql.interfaces.IRouteLocationDao;

import com.solvd.navigator.service.NavigationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        CliController cli = new CliController(navigationService);
        cli.start();
    }
}