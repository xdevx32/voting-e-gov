package com.nbu.evote;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenCSVReader {
    private static final String SAMPLE_CSV_FILE_PATH = "src/main/resources/csv/partynum.csv";

    public static void main(String[] args) throws IOException, CsvValidationException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println("Party : " + nextRecord[0]);
                System.out.println("Number : " + nextRecord[1]);
                System.out.println("==========================");
            }
        }
    }
}