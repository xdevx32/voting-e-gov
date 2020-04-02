package com.nbu.evote;

import com.nbu.evote.entity.Party;
import com.nbu.evote.service.PartyService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

public class CSVReaderAndParser {

    private static PartyService partyService;

//        private static final String SAMPLE_CSV_FILE_PATH = "src/main/resources/csv/partynum.csv";
        private static final String SAMPLE_CSV_FILE_PATH = "src/main/resources/csv/uploaded/";

        public static void invoke(String filename)throws IOException, CsvValidationException {

            //TODO FIX CRASH
            try (
                    Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH + filename));
                    CSVReader csvReader = new CSVReader(reader);
            ) {
                // Reading Records One by One in a String array
                String[] nextRecord;
                while ((nextRecord = csvReader.readNext()) != null) {
//                    System.out.println("Party : " + nextRecord[0]);
//                    System.out.println("Number : " + nextRecord[1]);
//                    System.out.println("==========================");
                    if(!nextRecord[1].equals("number")) {
//                        party.setName(nextRecord[0]);
//                        party.setNumber(nextRecord[1]);
                        //TODO FIX BUG NULLPOINTER EXCEPTION
                        Party party = new Party();
                        party.setName("wat");
                        party.setNumber("12");
                        party.setBallots(new HashSet<>());
                        partyService.addParty(party);
                    }


                }
            }
        }
    }

