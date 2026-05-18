package com.bibik.airport.state;


import com.bibik.airport.entity.impl.Airplane;

public class ArrivedState implements AirplaneState {

    @Override
    public void next(Airplane airplane) {
        airplane.setState(new BoardingState());
    }

    @Override
    public String getStatus() {
        return "Arrived / Awaiting Gate";
    }
}