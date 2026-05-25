package com.bibik.airport.state;

import com.bibik.airport.entity.Plane;

public interface PlaneState {
    void next(Plane airplane);
}

