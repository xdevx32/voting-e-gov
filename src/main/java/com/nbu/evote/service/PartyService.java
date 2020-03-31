package com.nbu.evote.service;

import com.nbu.evote.entity.Party;
import com.nbu.evote.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PartyService {


    @Autowired
    private PartyRepository partyRepository;

    public ArrayList<Party> getAllParties() {
        ArrayList<Party> parties = new ArrayList<>();
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
                .orElseThrow(() -> new IllegalArgumentException("Невалидно ID на партия: " + id));
        partyRepository.delete(party);
    }


}
