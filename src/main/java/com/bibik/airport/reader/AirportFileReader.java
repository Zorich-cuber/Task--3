package com.bibik.airport.reader;

import java.util.List;

public interface AirportFileReader {
    List<String> readLines(String filePath);

}
