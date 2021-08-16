package org.feelfee.core.controller;

import lombok.Builder;
import lombok.Data;
import org.feelfee.core.model.AdvContent;
import org.feelfee.core.service.AdvService;
import org.feelfee.core.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ogc")
@CrossOrigin
public class OpenGraphContentController {


    private final AdvService advService;

    private final ImageService imageService;

    @Autowired
    public OpenGraphContentController(AdvService advService, ImageService imageService) {
        this.advService = advService;
        this.imageService = imageService;
    }

    //todo получить постоянную ссылку на headerPhoto(учесть при загрузке,
// что картинка должна быть публичной)
    @GetMapping("/getByUrl/{url}")
    public OpenGraphContent getAdvByUrl(@PathVariable String url) {

        AdvContent advContent = advService.getContentByUrl(url);
        System.out.println("advContent:");
        System.out.println(advContent);
        String urlLink = imageService.getSharePhotoUrl(url);
        return OpenGraphContent.builder()
                .title(advContent.getDescHead())
                .description(advContent.getShareText())
                .imageLink(urlLink)
                .build();
    }

    @GetMapping("/getByUniqUrl/{uniqUrl}")
    public OpenGraphContent getByUniqUrl(@PathVariable String uniqUrl) {

        AdvContent advContent = advService.getContentByUniqUrl(uniqUrl);
        System.out.println("advContent:");
        String advUrl = advService.getUrlByUniqUrl(uniqUrl);
//        String imageLink = imageService.getHeaderPhotoUrl(advUrl);
        String imageLink = imageService.getSharePhotoUrl(advUrl);

        return OpenGraphContent.builder()
                .title(advContent.getDescHead())
                .description(advContent.getDescription())
                .imageLink(imageLink)
                .build();
    }


    @GetMapping("/image/downloadHeaderPhoto")
    public byte[] getHeaderPhoto(@RequestParam String url) {
        return imageService.getHeaderPhoto(url);
    }

    @GetMapping("/image/downloadHeaderPhotoUrl")
    public String getHeaderPhotoUrl(@RequestParam String url) {
        return imageService.getHeaderPhotoUrl(url);
    }


    @Data
    @Builder
    private static class OpenGraphContent {

        private String title;

        private String description;

        private String imageLink;
    }
}
