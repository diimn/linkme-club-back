package org.feelfee.core.service;

import lombok.RequiredArgsConstructor;
import org.feelfee.core.model.Partner;
import org.feelfee.core.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PartnerService {

    private final PartnerRepository partnerRepository;


    public Partner saveByNameAndPhone(String name, String phone) {
        Optional<Partner> optionalPartner
                = partnerRepository.findByNameAndPhone(name, phone);
        return optionalPartner.orElseGet(() -> {
            Partner partner = new Partner(UUID.randomUUID().toString(), name, phone);
            return partnerRepository.save(partner);
        });
    }

    public Partner save(Partner partner) {
        Optional<Partner> optionalPartner
                = partnerRepository.findByNameAndPhone(partner.getName(), partner.getPhone());
        return optionalPartner.orElseGet(() -> {
            partner.setId(UUID.randomUUID().toString());
            return partnerRepository.save(partner);
        });
    }


}
