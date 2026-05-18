package com.bibik.airport.parser.impl;

import com.bibik.airport.entity.impl.Airplane;
import com.bibik.airport.parser.AirportDataParser;
import com.bibik.airport.reader.AirportFileReader;
import com.bibik.airport.reader.impl.AirportFileReaderImpl;
import com.bibik.airport.util.AirportLogger;

import java.util.ArrayList;
import java.util.List;

public class AirportDataParserImpl implements AirportDataParser {
    private final AirportFileReaderImpl reader;

    public AirportDataParserImpl() {
        this.reader = new AirportFileReaderImpl();
    }

    @Override
    public List<Airplane> parseAirplanes(String filePath) {
        List<Airplane> airplanes = new ArrayList<>();
        List<String> lines = reader.readLines(filePath);

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            try {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    int capacity = Integer.parseInt(parts[0].trim());
                    int passengers = Integer.parseInt(parts[1].trim());
                    airplanes.add(new Airplane(capacity, passengers));
                }
            } catch (NumberFormatException e) {
                AirportLogger.warn("Invalid data format skipped: " + line);
            }
        }
        return airplanes;
    }
}

