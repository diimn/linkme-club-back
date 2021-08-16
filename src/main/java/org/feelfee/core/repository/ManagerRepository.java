package org.feelfee.core.repository;

import org.feelfee.core.model.Adv;
import org.feelfee.core.model.Manager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
//public interface AdvRepository extends JpaRepository<Adv, UUID> {
public interface ManagerRepository extends CrudRepository<Manager, String> {

     Boolean existsByLoginAndPassword(String login, String password);

     Optional<Manager> findByLoginAndPassword(String login, String password);

     Optional<Manager> findByLogin(String login);
}
