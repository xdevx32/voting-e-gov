package com.example.egov.voting.prototype.demo.egov.voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {


    @Autowired
    private CandidateRepository candidateRepository;

    public List<Candidate> getAllEmployees() {
        List<Candidate> candidates = new ArrayList<>();
        candidateRepository.findAll()
                .forEach(candidates::add);

        return candidates;
    }


    public Candidate getCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невалиден номер на кандидат: " + id));
        return candidate;
    }

    public void addCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public void updateCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невалидно ID на кандидат: " + id));
        candidateRepository.delete(candidate);
    }


}
