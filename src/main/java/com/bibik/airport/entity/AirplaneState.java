package com.bibik.airport.entity;

import com.bibik.airport.entity.impl.Airplane;

public interface AirplaneState {
        void next(Airplane airplane);
        String getStatus();
    }

