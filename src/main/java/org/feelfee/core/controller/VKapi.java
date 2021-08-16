package org.feelfee.core.controller;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.feelfee.core.service.VKapiService;
import org.feelfee.core.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/vk")
@CrossOrigin("*")
public class VKapi {

    private final VKapiService vKapiService;

    TransportClient transportClient = HttpTransportClient.getInstance();
    VkApiClient vk = new VkApiClient(transportClient);

    @Autowired
    public VKapi(VKapiService vKapiService) {
        this.vKapiService = vKapiService;
    }

    @GetMapping("/auth")
    public UserProfile getUserProfile(@RequestParam String code) throws ClientException, ApiException {
            return vKapiService.getUserProfile(code);
    }

    @GetMapping("/post")
    public String post(@RequestParam String userId, @RequestParam String message) throws ClientException, ApiException {
        return vKapiService.post(userId, message);
    }

}
