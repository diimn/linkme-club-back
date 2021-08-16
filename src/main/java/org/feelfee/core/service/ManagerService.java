package org.feelfee.core.service;

import org.feelfee.core.exception.ResourceNotFoundException;
import org.feelfee.core.model.AdvContent;
import org.feelfee.core.model.Manager;
import org.feelfee.core.model.Repost;
import org.feelfee.core.repository.AdvContentRepository;
import org.feelfee.core.repository.AdvRepository;
import org.feelfee.core.repository.ManagerRepository;
import org.feelfee.core.repository.RepostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
//    private final AdvRepository advRepository;
//    private final RepostRepository repostRepository;

    @Autowired
    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Boolean existsByLoginAndPassword(String login, String password) {
        return managerRepository.existsByLoginAndPassword(login, password);
    }

    public Optional<Manager> findByLoginAndPassword(String login, String password) {
        return managerRepository.findByLoginAndPassword(login, password);
    }

    public Optional<Manager> findByLogin(String login) {
        return managerRepository.findByLogin(login);
    }
}
