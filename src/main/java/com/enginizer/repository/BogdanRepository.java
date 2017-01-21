package com.enginizer.repository;

import com.enginizer.model.entities.Bogdan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by badmotherfucker on 1/21/17.
 */


@Transactional
@Repository
public interface BogdanRepository extends CrudRepository<Bogdan, String>{
}
