package com.nbu.evote.utility;

import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CSVReaderAndParser {

    @Autowired
    private PartyService partyService;

    @Autowired
    CitizenService citizenService;

    public CSVReaderAndParser(PartyService partyService) {
        this.partyService = partyService;
    }

    public CSVReaderAndParser(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    private static final String SAMPLE_CSV_FILE_PATH = "src/main/resources/csv/uploaded/";

    public void invokeCitizensUpload(String filename)throws IOException, CsvValidationException {

        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH + filename));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            ArrayList<Citizen> listOfRecords = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {

                if(!nextRecord[0].equals("name")) {
                    Citizen citizen = new Citizen();

                    citizen.setName(nextRecord[0]);
                    citizen.setEGN(nextRecord[1]);
                    citizen.setUniqueVoteId(nextRecord[2]);
                    citizen.setCity(nextRecord[3]);
                    LocalDate dateOfBirth =LocalDate.parse(nextRecord[4]);
                    citizen.setDayOfBirth(dateOfBirth);
                    citizen.setHasVoted(Boolean.valueOf(nextRecord[5]));
                    listOfRecords.add(citizen);
                }
            }
            saveCitizensRecords(listOfRecords);
        }
    }

    public void invokePartiesUpload(String filename)throws IOException, CsvValidationException {

        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH + filename));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            ArrayList<Party> listOfRecords = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {

                if(!nextRecord[1].equals("number")) {
                    Party party = new Party();

                    party.setName(nextRecord[0]);
                    party.setNumber(nextRecord[1]);
                    party.setSlogan(nextRecord[2]);
                    party.setImageUrl(nextRecord[3]);
                    party.setGroupImageUrl(nextRecord[4]);
                    listOfRecords.add(party);
                }
            }
            savePartyRecords(listOfRecords);
        }
    }

    private void savePartyRecords(ArrayList<Party> listOfRecords) {
        if(listOfRecords.get(0) instanceof Party) {
            listOfRecords.stream().forEach(x -> {
                partyService.addParty(x);
            });
        }
    }

    private void saveCitizensRecords(ArrayList<Citizen> listOfRecords) {
        if(listOfRecords.get(0) instanceof Citizen) {
            listOfRecords.stream().forEach(x -> {
                citizenService.addCitizen(x);
            });
        }
    }

    //TODO: Fix duplication of code.
}

