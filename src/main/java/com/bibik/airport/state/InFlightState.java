package com.bibik.airport.state;

import com.bibik.airport.entity.Plane;

public class InFlightState implements PlaneState {

    @Override
    public void next(Plane plane) {
        plane.setPlaneState(new ArrivedState());
    }

    @Override
    public String toString() {
        return "IN_FLIGHT";
    }
}