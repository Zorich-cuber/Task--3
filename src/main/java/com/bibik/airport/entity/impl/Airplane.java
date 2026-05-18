package com.bibik.airport.entity.impl;


import com.bibik.airport.entity.AirplaneState;
import com.bibik.airport.state.ArrivedStateImpl;
import java.util.concurrent.atomic.AtomicInteger;

public class Airplane {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final int id;
    private final int capacity;
    private final int passengers;
    private AirplaneState currentState;

    public Airplane(int capacity, int passengers) {
        this.id = idGenerator.incrementAndGet();
        this.capacity = capacity;
        this.passengers = Math.min(passengers, capacity);
        this.currentState = new ArrivedStateImpl();
    }

    public void setState(AirplaneState state) {
        this.currentState = state;
    }

    public void performAction() {
        currentState.handle(this);
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPassengers() {
        return passengers;
    }

    public AirplaneState getCurrentState() {
        return currentState;
    }
}