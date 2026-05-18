package com.bibik.airport.reader.impl;

import com.bibik.airport.reader.AirportFileReader;
import com.bibik.airport.util.AirportLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class AirportFileReaderImpl implements AirportFileReader {
    @Override
    public List<String> readLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            AirportLogger.error("Failed to read file: " + filePath);
            return Collections.emptyList();
        }
    }
}

