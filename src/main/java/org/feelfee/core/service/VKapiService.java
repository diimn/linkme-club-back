package org.feelfee.core.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.users.UserField;
import org.feelfee.core.Globals;
import org.feelfee.core.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.feelfee.core.Globals.*;

@Service
public class VKapiService {

    private final UserProfileService userProfileService;
    private final Globals globals;

    TransportClient transportClient = HttpTransportClient.getInstance();

    VkApiClient vk = new VkApiClient(transportClient);

    @Autowired
    public VKapiService(UserProfileService userProfileService, Globals globals) {
        this.userProfileService = userProfileService;
        this.globals = globals;
    }

    public UserProfile getUserProfile(String code) throws ClientException, ApiException {
        UserAuthResponse authResponse = vk.oauth()
                .userAuthorizationCodeFlow(Integer.valueOf(APP_ID), CLIENT_SECRET, globals.getRedirectUrl(), code)
                .execute();

        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
        UserProfile userProfile;
        Optional<UserProfile> userProfileOptional
                = userProfileService.getUserProfileBySocialIdAndSocialType(actor.getId().toString(), "VK");
        if (userProfileOptional.isPresent()) {
            userProfile = userProfileOptional.get();
            fillVariablePart(actor, userProfile);
        } else {
            userProfile = new UserProfile();
            userProfile.setId(UUID.randomUUID().toString());
            userProfile.setSocialId(actor.getId().toString());
            userProfile.setSocialType("VK");
            fillVariablePart(actor, userProfile);
        }
        return userProfileService.createOrUpdateUserProfile(userProfile);
    }

    public String post(String userId, String message) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileBySocialIdAndSocialType(userId, "VK");
        AtomicReference<String> response = new AtomicReference<>();
        userProfile.ifPresent(userProfile1 -> {
            UserActor actor = new UserActor(Integer.valueOf(userId), userProfile1.getAccessToken());
            try {
                PostResponse resp = vk.wall()
                        .post(actor)
                        .friendsOnly(false)
                        .fromGroup(false)
                        .message(message)
                        .execute();
                response.set(resp.getPostId().toString());
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        });
        return response.get();
    }


    private void fillVariablePart(UserActor actor, UserProfile userProfile) throws ClientException, ApiException {
        userProfile.setAccessToken(actor.getAccessToken());
        List<UserXtrCounters> usersVk;
        UserXtrCounters userVK = null;
        usersVk = vk.users()
                .get(actor)
                .fields(UserField.PHOTO_MAX)
                .execute();
        if (usersVk != null && !usersVk.isEmpty()) {
            userVK = usersVk.get(0);
            userProfile.setUserName(userVK.getFirstName() + " " + userVK.getLastName());
            userProfile.setUserProfileImageLink(userVK.getPhotoMax());
        }

    }


}
