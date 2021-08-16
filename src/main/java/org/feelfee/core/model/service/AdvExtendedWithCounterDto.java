package org.feelfee.core.model.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.feelfee.core.model.Adv;
import org.feelfee.core.model.AdvContent;
import org.feelfee.core.model.Manager;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AdvExtendedWithCounterDto extends Adv{

    int buyCounter;

    int loginCounter;

    int repostCounter;

    public AdvExtendedWithCounterDto(Adv adv, int buyCounter, int loginCounter, int repostCounter) {
        this.setAdvContent(adv.getAdvContent());
        this.setCreatingDate(adv.getCreatingDate());
        this.setIsMainPageActive(adv.getIsMainPageActive());
        this.setManager(adv.getManager());
        this.setUrl(adv.getUrl());
        this.setId(adv.getId());
        this.setBuyCounter(buyCounter);
        this.setLoginCounter(loginCounter);
        this.setRepostCounter(repostCounter);
    }

}
