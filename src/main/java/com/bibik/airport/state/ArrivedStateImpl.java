package com.bibik.airport.state;
import com.bibik.airport.entity.AirplaneState;
import com.bibik.airport.entity.impl.Airplane;
import com.bibik.airport.util.AirportLogger;

import java.util.concurrent.TimeUnit;


public class ArrivedStateImpl implements AirplaneState {
    @Override
    public void handle(Airplane airplane) {
        AirportLogger.info("Airplane #" + airplane.getId() + " has arrived at the airport.");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public AirplaneState getStatus() {
        return this;
    }
}
