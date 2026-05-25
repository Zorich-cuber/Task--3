package com.bibik.airport.state;

import com.bibik.airport.entity.Plane;

import java.util.concurrent.TimeUnit;

public class BoardingStateImpl implements PlaneState {

    @Override
    public void next(Plane airplane) {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}