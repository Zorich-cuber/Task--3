package com.bibik.airport.entity.impl;

import com.bibik.airport.entity.AirplaneState;
import com.bibik.airport.state.ArrivedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Airplane implements Callable<Void> {
    private static final Logger logger = LogManager.getLogger(Airplane.class);
    private final String name;
    private final int maxCapacity;
    private int currentPassengers;
    private AirplaneState state;

    public Airplane(String name, int currentPassengers, int maxCapacity) {
        this.name = name;
        this.currentPassengers = currentPassengers;
        this.maxCapacity = maxCapacity;
        this.state = new ArrivedState();
    }

    public void setState(AirplaneState state) {
        this.state = state;
    }

    public String getName() { return name; }
    public int getCurrentPassengers() { return currentPassengers; }
    public int getMaxCapacity() { return maxCapacity; }

    @Override
    public Void call() throws Exception {
        logger.info("Airplane {} started execution. Status: {}", name, state.getStatus());
        AirportTerminal terminal = AirportTerminal.getInstance();
        Gate gate = terminal.acquireGate();

        try {
            state.next(this); // Transition to BoardingState
            logger.info("Airplane {} occupied gate {}. Status: {}", name, gate.getId(), state.getStatus());

            terminal.disembarkPassengers(this);
            TimeUnit.SECONDS.sleep(1);

            terminal.boardPassengers(this);
            TimeUnit.SECONDS.sleep(1);

            state.next(this); // Transition to DepartedState
            logger.info("Airplane {} successfully processed and departed. Status: {}", name, state.getStatus());
        } finally {
            terminal.releaseGate(gate);
        }
        return null;
    }

    public void setPassengersAfterBoarding(int count) {
        this.currentPassengers = count;
    }
}