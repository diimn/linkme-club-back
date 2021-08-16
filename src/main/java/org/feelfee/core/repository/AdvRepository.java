package org.feelfee.core.repository;

import org.feelfee.core.model.Adv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//public interface AdvRepository extends JpaRepository<Adv, UUID> {
public interface AdvRepository extends JpaRepository<Adv, String> {

     @Query("select p.id from Adv p where p.isMainPageActive = true")
//     @Query("select p.id from Adv p")
     List<String> getAllActiveIds();

     Page<Adv> findAllByManager_Id(String managerId, Pageable pageable);
     Page<Adv> findAll(Pageable pageable);
     List<Adv> findAllByManager_Id(String managerId);

     Optional<Adv> findAdvByUrl(String url);
     Optional<Adv> findAdvById(String id);
     Boolean existsAdvByUrl(String url);
}
