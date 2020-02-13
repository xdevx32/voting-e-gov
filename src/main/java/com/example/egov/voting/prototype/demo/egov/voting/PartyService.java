package com.example.egov.voting.prototype.demo.egov.voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartyService {


    @Autowired
    private PartyRepository partyRepository;

    public List<Party> getAllParties() {
        List<Party> parties = new ArrayList<>();
        partyRepository.findAll()
                .forEach(parties::add);

        return parties;
    }


    public Party getParty(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невалиден номер на кандидат: " + id));
        return party;
    }

    public void addParty(Party party) {
        partyRepository.save(party);
    }

    public void updateParty(Party party) {
        partyRepository.save(party);
    }

    public void deleteParty(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невалидно ID на партияс: " + id));
        partyRepository.delete(party);
    }


}
