package com.bibik.airport.entity;

public class Gate {
    private final int gateId;
    private boolean isOccupied;

    public Gate(int gateId) {
        this.gateId = gateId;
        this.isOccupied = false;
    }

    public int getGateId() {
        return gateId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }
}
