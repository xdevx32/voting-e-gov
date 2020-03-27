package com.nbu.egov.repository;
import com.nbu.egov.entity.Party;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface PartyRepository extends CrudRepository<Party, Long>  {

    @Override
    ArrayList<Party> findAll();

    @Override
    Optional<Party> findById(Long aLong);

}
