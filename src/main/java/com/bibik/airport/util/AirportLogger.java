package com.bibik.airport.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirportLogger {
    private static final Logger logger = LogManager.getLogger("Airport");

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }
}


