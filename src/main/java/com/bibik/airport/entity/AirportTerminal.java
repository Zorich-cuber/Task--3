package com.bibik.airport.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirportTerminal {
    private static final Logger logger = LogManager.getLogger(AirportTerminal.class);

    private int maxTerminalCapacity;
    private int currentPassengersInTerminal = 0;

    private final List<Gate> gates = new ArrayList<>();

    private final Lock lock = new ReentrantLock(true);
    private final Condition conditionGateAvailable = lock.newCondition();
    private final Condition conditionTerminalSpaceAvailable = lock.newCondition();

    private AirportTerminal() {}

    private static class Holder {
        private static final AirportTerminal INSTANCE = new AirportTerminal();
    }

    public static AirportTerminal getInstance() {
        return Holder.INSTANCE;
    }

    public void configure(int totalGates, int terminalCapacity) {
        lock.lock();
        try {
            this.maxTerminalCapacity = terminalCapacity;
            this.gates.clear();
            for (int i = 1; i <= totalGates; i++) {
                this.gates.add(new Gate(i));
            }
            logger.info("Airport terminal configured. Total gates: {}, Max capacity: {}", totalGates, terminalCapacity);
        } finally {
            lock.unlock();
        }
    }

    private Gate findFreeGate() {
        for (Gate gate : gates) {
            if (!gate.isOccupied()) {
                return gate;
            }
        }
        return null;
    }

    private int getFreeGatesCount() {
        int count = 0;
        for (Gate gate : gates) {
            if (!gate.isOccupied()) {
                count++;
            }
        }
        return count;
    }

    public void processPlane(Plane plane) throws InterruptedException {
        String id = plane.getPlaneId();
        int incomingPassengers = plane.getPassengersCount();
        Gate assignedGate = null;

        lock.lock();
        try {
            while (getFreeGatesCount() == 0 || (currentPassengersInTerminal + incomingPassengers > maxTerminalCapacity)) {
                if (getFreeGatesCount() == 0) {
                    logger.info("Plane [{}] is waiting for a free gate. Free gates: 0", id);
                    conditionGateAvailable.await();
                } else {
                    logger.info("Plane [{}] is waiting for terminal space. Required: {}, Current: {}/{}",
                            id, incomingPassengers, currentPassengersInTerminal, maxTerminalCapacity);
                    conditionTerminalSpaceAvailable.await();
                }
            }

            assignedGate = findFreeGate();
            if (assignedGate != null) {
                assignedGate.setOccupied(true);
            }

            plane.getPlaneState().next(plane);
            currentPassengersInTerminal += incomingPassengers;

            logger.info("Plane [{}] docked at Gate #{}. Status: {}. Disembarked {} passengers. Terminal capacity: {}/{}",
                    id, assignedGate.getGateId(), plane.getPlaneState(), incomingPassengers, currentPassengersInTerminal, maxTerminalCapacity);

        } finally {
            lock.unlock();
        }

        TimeUnit.MILLISECONDS.sleep(1000);

        lock.lock();
        try {
            int outgoingPassengers = Math.min(plane.getMaxCapacity(), currentPassengersInTerminal);
            currentPassengersInTerminal -= outgoingPassengers;
            plane.setPassengersCount(outgoingPassengers);

            plane.getPlaneState().next(plane);

            if (assignedGate != null) {
                assignedGate.setOccupied(false);
            }

            logger.info("Plane [{}] left Gate #{}, boarded {} passengers and departed. Status: {}. Free gates: {}/{}",
                    id, assignedGate.getGateId(), outgoingPassengers, plane.getPlaneState(), getFreeGatesCount(), gates.size());

            conditionGateAvailable.signalAll();
            conditionTerminalSpaceAvailable.signalAll();

        } finally {
            lock.unlock();
        }
    }
}
