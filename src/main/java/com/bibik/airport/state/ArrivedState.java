package com.bibik.airport.state;

import com.bibik.airport.entity.Plane;

public class ArrivedState implements PlaneState {

    @Override
    public void next(Plane plane) {
        plane.setPlaneState(new DepartedState());
    }

    @Override
    public String toString() {
        return "ARRIVED";
    }
}