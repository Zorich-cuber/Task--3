package com.bibik.airport.entity;

import com.bibik.airport.state.InFlightState;
import com.bibik.airport.state.PlaneState;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Plane implements Callable<String> {
    private final String planeId;
    private final int maxCapacity;
    private int passengersCount;
    private PlaneState planeState;
    private final Lock stateLock = new ReentrantLock();

    public Plane(String planeId, int maxCapacity, int initialPassengers) {
        this.planeId = planeId;
        this.maxCapacity = maxCapacity;
        this.passengersCount = Math.min(initialPassengers, maxCapacity);
        this.planeState = new InFlightState();
    }

    @Override
    public String call() throws Exception {
        AirportTerminal terminal = AirportTerminal.getInstance();
        terminal.processPlane(this);
        return "Thread " + planeId + " execution finished.";
    }

    public String getPlaneId() {
        return planeId;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getPassengersCount() {
        stateLock.lock();
        try {
            return passengersCount;
        } finally {
            stateLock.unlock();
        }
    }

    public void setPassengersCount(int passengersCount) {
        stateLock.lock();
        try {
            this.passengersCount = passengersCount;
        } finally {
            stateLock.unlock();
        }
    }

    public PlaneState getPlaneState() {
        stateLock.lock();
        try {
            return planeState;
        } finally {
            stateLock.unlock();
        }
    }

    public void setPlaneState(PlaneState planeState) {
        stateLock.lock();
        try {
            this.planeState = planeState;
        } finally {
            stateLock.unlock();
        }
    }
}
