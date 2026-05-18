package com.bibik.airport.parser;

import com.bibik.airport.entity.impl.Airplane;

import java.util.List;

public interface AirportDataParser {
    List<Airplane> parseAirplanes(String filePath);
}
