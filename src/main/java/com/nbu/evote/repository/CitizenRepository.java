package com.nbu.evote.repository;

import com.nbu.evote.entity.Citizen;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CitizenRepository extends CrudRepository<Citizen, Long>{

    @Query(value = "SELECT * FROM citizen a WHERE unique_vote_id = ?1 AND egn = ?2", nativeQuery = true)
    Citizen findByEGNAndUniqueVoteId(String uniqueVoteId, String EGN);
}
