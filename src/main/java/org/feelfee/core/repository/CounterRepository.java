package org.feelfee.core.repository;


import org.feelfee.core.model.AdvCounter;
import org.feelfee.core.model.Repost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounterRepository extends CrudRepository<AdvCounter, String> {

    Optional<AdvCounter> findByAdv_Url(String url);

}
