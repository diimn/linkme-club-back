package org.feelfee.core.model.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.feelfee.core.model.AdvContent;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdvUpload {

    String headPhoto;

    List<Slider> slider1;

    List<Slider> slider2;

    String brokerPhoto;

    String url;

    Boolean isMainPageActive;

    AdvContent advContent;

    String shareImage;

    @ToString
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Slider {
        int index;
        String content;
    }

}
