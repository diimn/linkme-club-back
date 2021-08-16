package org.feelfee.core.service;

import com.amazonaws.services.medialive.model.ScheduleActionSettings;
import org.feelfee.core.filestore.FileStore;
import org.feelfee.core.model.Adv;
import org.feelfee.core.model.AdvCounter;
import org.feelfee.core.model.Repost;
import org.feelfee.core.model.service.Counter;
import org.feelfee.core.repository.AdvRepository;
import org.feelfee.core.repository.CounterRepository;
import org.feelfee.core.repository.RepostRepository;
import org.feelfee.core.repository.UserProfileRepository;
import org.feelfee.core.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CounterService {

    private final RepostRepository repostRepository;
    private final AdvRepository advRepository;
    private final CounterRepository counterRepository;


    @Autowired
    public CounterService(RepostRepository repository, AdvRepository advRepository, CounterRepository counterRepository) {
        this.repostRepository = repository;
        this.advRepository = advRepository;
        this.counterRepository = counterRepository;
    }

    public AdvCounter getAdvCounter(String subdomain) {
        return counterRepository.findByAdv_Url(subdomain).orElse(new AdvCounter());
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void incrementCounterSubDomain(String subDomain, Counter counter) {
        AdvCounter advCounter = getCounterBySubdomain(subDomain);
        incrementCounter(advCounter, counter);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void incrementCounterUniqUrl(String uniqUrl, Counter counter) {
        AdvCounter advCounter = getCounterByUniqUrl(uniqUrl);
        incrementCounter(advCounter, counter);
    }

    private void incrementCounter(AdvCounter advCounter, Counter counter) {
        if (advCounter != null) {
            switch (counter) {
                case BUY:
                    advCounter.setBuyCounter(advCounter.getBuyCounter() + 1);
                    break;
                case LOGIN:
                    advCounter.setLoginCounter(advCounter.getLoginCounter() + 1);
                    break;
                case REPOST:
                    advCounter.setRepostCounter(advCounter.getRepostCounter() + 1);
                    break;
            }
            counterRepository.save(advCounter);
        }
    }


    private AdvCounter getCounterBySubdomain(String subDomain) {
        if (subDomain != null && !subDomain.isEmpty()) {
            Optional<AdvCounter> val = counterRepository.findByAdv_Url(subDomain);
            if (val.isPresent()) {
                return val.get();
            }
        }
        Adv adv = advRepository.findAdvByUrl(subDomain).get();
        return new AdvCounter(UUID.randomUUID().toString(), 0, 0, 0, adv);
    }

    private AdvCounter getCounterByUniqUrl(String uniqUrl) {
        if (uniqUrl != null && !uniqUrl.isEmpty()) {
            Optional<Repost> repost = repostRepository.findByUniqLink(uniqUrl);
            if (repost.isPresent()) {
                return getCounterBySubdomain(repost.get().getAdv().getUrl());
            }
        }
        return null;
    }
}
