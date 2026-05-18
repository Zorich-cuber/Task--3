package com.bibik.airport.entity.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AirportTerminal {
    private static final Logger logger = LogManager.getLogger(AirportTerminal.class);

    // Thread-safe Lazy Initialization Holder (No enum, synchronized, or volatile used)
    private static class Holder {
        private static final AirportTerminal INSTANCE = new AirportTerminal();
    }

    public static AirportTerminal getInstance() {
        return Holder.INSTANCE;
    }

    private final Lock lock = new ReentrantLock();
    private final Condition gateAvailable = lock.newCondition();
    private final Condition spaceAvailable = lock.newCondition();
    private final Condition passengersAvailable = lock.newCondition();

    private int maxTerminalCapacity;
    private int currentTerminalPassengers;
    private final List<Gate> freeGates = new ArrayList<>();

    private AirportTerminal() {}

    public void init(int maxCapacity, int gateCount) {
        this.maxTerminalCapacity = maxCapacity;
        for (int i = 1; i <= gateCount; i++) {
            freeGates.add(new Gate(i));
        }
    }

    public Gate acquireGate() throws InterruptedException {
        lock.lock();
        try {
            while (freeGates.isEmpty()) {
                gateAvailable.await();
            }
            return freeGates.remove(0);
        } finally {
            lock.unlock();
        }
    }

    public void releaseGate(Gate gate) {
        lock.lock();
        try {
            freeGates.add(gate);
            gateAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public void disembarkPassengers(Airplane airplane) throws InterruptedException {
        lock.lock();
        try {
            int leaving = airplane.getCurrentPassengers();
            while (currentTerminalPassengers + leaving > maxTerminalCapacity) {
                logger.warn("Terminal is full! Airplane {} is waiting to disembark passengers.", airplane.getName());
                spaceAvailable.await();
            }
            currentTerminalPassengers += leaving;
            logger.info("Disembarked {} passengers from airplane {}. Total in terminal: {}",
                    leaving, airplane.getName(), currentTerminalPassengers);
            passengersAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void boardPassengers(Airplane airplane) throws InterruptedException {
        lock.lock();
        try {
            int desiredCount = airplane.getMaxCapacity();
            while (currentTerminalPassengers < desiredCount && currentTerminalPassengers == 0) {
                logger.warn("No passengers available in terminal for airplane {}. Waiting...", airplane.getName());
                passengersAvailable.await();
            }

            int boarding = Math.min(desiredCount, currentTerminalPassengers);
            currentTerminalPassengers -= boarding;
            airplane.setPassengersAfterBoarding(boarding);

            logger.info("Boarded {} passengers onto airplane {}. Remaining in terminal: {}",
                    boarding, airplane.getName(), currentTerminalPassengers);
            spaceAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }
}