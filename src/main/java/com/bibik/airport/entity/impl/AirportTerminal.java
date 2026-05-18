package com.bibik.airport.entity.impl;


import com.bibik.airport.util.AirportLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AirportTerminal {
    private static class Holder {
        private static final AirportTerminal INSTANCE = new AirportTerminal(5); // Number of gates
    }

    private final List<Gate> gates;
    private final Semaphore semaphore;
    private final ReentrantLock allocationLock = new ReentrantLock();

    private AirportTerminal(int gateCount) {
        this.gates = new ArrayList<>();
        for (int i = 1; i <= gateCount; i++) {
            gates.add(new Gate(i));
        }
        this.semaphore = new Semaphore(gateCount, true); // fair semaphore
        AirportLogger.info("Airport initialized with " + gateCount + " gates.");
    }

    public static AirportTerminal getInstance() {
        return Holder.INSTANCE;
    }

    public Gate acquireGate(long timeout, TimeUnit unit) throws InterruptedException {
        if (!semaphore.tryAcquire(timeout, unit)) {
            throw new InterruptedException("Timeout while waiting for a free gate.");
        }

        allocationLock.lock();
        try {
            for (Gate gate : gates) {
                if (gate.isFree()) {
                    gate.occupy();
                    AirportLogger.info("Gate #" + gate.getId() + " allocated to airplane.");
                    return gate;
                }
            }
        } finally {
            allocationLock.unlock();
        }
        return null;
    }

    public void releaseGate(Gate gate) {
        if (gate != null) {
            gate.release();
            semaphore.release();
            AirportLogger.info("Gate #" + gate.getId() + " released.");
        }
    }
}