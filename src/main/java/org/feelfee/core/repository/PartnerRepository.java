package org.feelfee.core.repository;

import org.feelfee.core.model.Partner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends CrudRepository<Partner, String> {

    Optional<Partner> findByNameAndPhone(String name, String phone);

}
