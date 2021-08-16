package org.feelfee.core.model.service;

import lombok.*;
import org.feelfee.core.model.AdvContent;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MainPageRandomAdv {

    String url;

    String header;

    String commentHeader;

    String commentPrice;

    String price;

    String bonus;


}
