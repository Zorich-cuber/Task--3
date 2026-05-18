package com.bibik.airport.state;

import com.bibik.airport.entity.AirplaneState;
import com.bibik.airport.entity.impl.Airplane;
import com.bibik.airport.util.AirportLogger;

public class DepartedStateImpl implements AirplaneState {

    @Override
    public void handle(Airplane airplane) {
        AirportLogger.info("Airplane #" + airplane.getId() + " has departed.");
    }

    @Override
    public AirplaneState getStatus() {
        return this;
    }
}
