package org.feelfee.core.controller;

import org.feelfee.core.model.ClientResponse;
import org.feelfee.core.model.Repost;
import org.feelfee.core.service.ClientResponseService;
import org.feelfee.core.service.RepostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/clientResponse")
@CrossOrigin
public class ClientResponseController {

    private final ClientResponseService clientResponseService;

    @Autowired
    public ClientResponseController(ClientResponseService clientResponseService) {
        this.clientResponseService = clientResponseService;
    }

    @PostMapping("/post")
    public String post(@RequestBody ClientResponse response) {
        System.out.println(response);
        clientResponseService.save(response);
        return "POST OK";
    }

}
