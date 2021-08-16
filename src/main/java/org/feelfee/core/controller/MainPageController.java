package org.feelfee.core.controller;

import org.feelfee.core.model.Repost;
import org.feelfee.core.service.RepostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/mainPage")
@CrossOrigin
public class MainPageController {

    private final RepostService repostService;

    @Autowired
    public MainPageController(RepostService repostService) {
        this.repostService = repostService;
    }

    @GetMapping("/getRepostOrSave")
    @CrossOrigin
    public Repost findOrSaveByUserProfile_SocialIdAndAdv_Url(@RequestParam String socialType,
                                                             @RequestParam String socialId,
                                                             @RequestParam String url) {
        return repostService.findOrSaveByUserProfile_SocialIdAndAdv_Url(socialId, url, socialType);
    }

    @GetMapping("/getRepostOrSaveByUniqUrl")
    @CrossOrigin
    public Repost findByUserProfile_SocialIdAndUniqLink(@RequestParam String socialType,
                                                        @RequestParam String socialId,
                                                        @RequestParam String uniqUrl) {
        return repostService.findByUserProfile_SocialIdAndUniqLink(socialType, socialId, uniqUrl);
    }


    @GetMapping("/findByUniqUrl")
    @CrossOrigin
    public Optional<Repost> findByUniqUrl(@RequestParam String url) {
        return repostService.findByUniqUrl(url);
    }

    @GetMapping("/incrementRepost")
    @CrossOrigin
    public Optional<Repost> incrementRepost(@RequestParam String url) {
        return repostService.incrementRepost(url);
    }

}
