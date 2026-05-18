package com.bibik.airport.state;

import com.bibik.airport.entity.AirplaneState;
import com.bibik.airport.entity.impl.Airplane;
import com.bibik.airport.util.AirportLogger;

import java.util.concurrent.TimeUnit;

public class BoardingStateImpl implements AirplaneState {

    @Override
    public void handle(Airplane airplane) {
        AirportLogger.info("Airplane #" + airplane.getId() +
                " is at the gate. Boarding / disembarking passengers...");

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public AirplaneState getStatus() {
        return this;
    }
}