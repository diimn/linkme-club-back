package org.feelfee.core.controller;

import lombok.RequiredArgsConstructor;
import org.feelfee.core.model.Partner;
import org.feelfee.core.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/newPartner")
    public String post(@RequestBody Partner partner) {
        System.out.println("newPartner request received");
        System.out.println(partner);
        partnerService.save(partner);
        return "POST OK";
    }

}
