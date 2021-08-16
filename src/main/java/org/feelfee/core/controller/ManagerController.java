package org.feelfee.core.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.feelfee.core.model.Manager;
import org.feelfee.core.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/manager")
@CrossOrigin
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> checkUser(@RequestBody String credentials) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        Manager manager = objectMapper.readValue(credentials, Manager.class);

        boolean isPresent = managerService.findByLoginAndPassword(manager.getLogin(), manager.getPassword()).isPresent();

        Algorithm algorithm = Algorithm.HMAC256("secret");

        String token = JWT.create()
                .withClaim("user", manager.getLogin())
                .withClaim("password", manager.getPassword())
                .sign(algorithm);
        Map<String, String> resp = new HashMap<>();
        resp.put("token", token);
        if (isPresent) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
