package org.feelfee.core.controller;

import org.feelfee.core.model.service.Counter;
import org.feelfee.core.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/counter")
@CrossOrigin
public class CounterController {

    private final CounterService counterService;

    @Autowired
    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping("/incrementRepostByUniq")
    public String incrementRepostByUniq(@RequestParam String url) {
        counterService.incrementCounterUniqUrl(url, Counter.REPOST);
        return "OK";
    }

    @GetMapping("/incrementRepostBySubdomain")
    public String incrementRepostBySubdomain(@RequestParam String url) {
        counterService.incrementCounterSubDomain(url, Counter.REPOST);
        return "OK";
    }

    @GetMapping("/incrementBuyByUniq")
    public String incrementBuyByUniq(@RequestParam String url) {
        counterService.incrementCounterUniqUrl(url, Counter.BUY);
        return "OK";
    }

    @GetMapping("/incrementBuyBySubdomain")
    public String incrementBuyBySubdomain(@RequestParam String url) {
        counterService.incrementCounterSubDomain(url, Counter.BUY);
        return "OK";
    }

    @GetMapping("/incrementLoginByUniq")
    public String incrementLoginByUniq(@RequestParam String url) {
        counterService.incrementCounterUniqUrl(url, Counter.LOGIN);
        return "OK";
    }

    @GetMapping("/incrementLoginBySubdomain")
    public String incrementLoginBySubdomain(@RequestParam String url) {
        counterService.incrementCounterSubDomain(url, Counter.LOGIN);
        return "OK";
    }

}
