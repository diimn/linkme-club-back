package org.feelfee.core.controller;

import org.feelfee.core.model.UserProfile;
import org.feelfee.core.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/getAll")
    public List<UserProfile> getUserProfiles() {
        return userProfileService.getUserProfiles();
    }

    @GetMapping("/getUserBySocialId")
    @CrossOrigin
    public Optional<UserProfile> getUserProfiles(@RequestParam String userSocialId,
                                                 @RequestParam String socialType) {
        return userProfileService.getUserProfileBySocialIdAndSocialType(userSocialId, socialType);
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                       @RequestParam("file") MultipartFile file) {
        userProfileService.upload(userProfileId, file);
    }

    @GetMapping("{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
        return userProfileService.downloadUserProfileImage(userProfileId);
    }

    @GetMapping("/getContent")
    public String getContent() {
        return "CONTENT";
    }


    @GetMapping("/save")
    public Optional<UserProfile> save(@RequestParam String socialType,
                                      @RequestParam String socialId,
                                      @RequestParam String userName,
                                      @RequestParam String imageLink,
                                      @RequestParam String accessToken) {

        UserProfile userProfile = new UserProfile();
        userProfile.setId(UUID.randomUUID().toString());
        userProfile.setSocialId(socialId);
        userProfile.setSocialType(socialType);
        userProfile.setUserName(userName);
        userProfile.setAccessToken(accessToken);
        userProfile.setUserProfileImageLink(imageLink);

        return Optional.ofNullable(userProfileService.createOrUpdateUserProfile(userProfile));
    }
}
