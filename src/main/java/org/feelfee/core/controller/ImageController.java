package org.feelfee.core.controller;

import org.feelfee.core.model.service.SliderWrapper;
import org.feelfee.core.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/image")
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/downloadHeaderPhoto")
    public byte[] getHeaderPhoto(@RequestParam String url) {
        return imageService.getHeaderPhoto(url);
    }

    @GetMapping("/downloadBrokerPhoto")
    public byte[] getBrokerPhoto(@RequestParam String url) {
        return imageService.getBrokerPhoto(url);
    }

    @GetMapping("/downloadSliderPhoto")
    public byte[] getSliderPhoto(@RequestParam String url, @RequestParam int index, @RequestParam String type) {
        return imageService.getSliderPhoto(url, index, type);
    }

    @GetMapping("/getNumberSliderPhotos")
    public int getNumberSliderPhotos(@RequestParam String url, @RequestParam String type) {
        return imageService.getNumberSliderPhotos(url, type);
    }

    @GetMapping("/getAllNumberSliderPhotos")
    public SliderWrapper getNumberSliderPhotos(@RequestParam String url) {
        return imageService.getNumberSliderPhotos(url);
    }


    @GetMapping("/test")
    public String testImage() {
        return "test";
    }
}
