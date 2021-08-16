package org.feelfee.core.repository;

import org.feelfee.core.model.AdvContent;
import org.feelfee.core.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdvContentRepository extends CrudRepository<AdvContent, String> {

}
