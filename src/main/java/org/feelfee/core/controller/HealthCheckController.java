package org.feelfee.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@CrossOrigin("*")
public class HealthCheckController {

    @GetMapping("/healthCheck")
    public ResponseEntity<String> getUserProfile() {
        return ResponseEntity.ok("OK3");
    }

}
