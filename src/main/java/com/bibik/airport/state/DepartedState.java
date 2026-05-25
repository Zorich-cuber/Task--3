package com.bibik.airport.state;

import com.bibik.airport.entity.Plane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartedState implements PlaneState {
    private static final Logger logger = LogManager.getLogger(DepartedState.class);

    @Override
    public void next(Plane plane) {
        logger.error("State transition error: Plane [{}] is already in DEPARTED state.", plane.getPlaneId());
    }

    @Override
    public String toString() {
        return "DEPARTED";
    }
}

