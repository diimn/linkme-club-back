package org.feelfee.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "adv", indexes = {@Index(name="ADV_INDEX",columnList = "id,url")})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="system-uuid" )
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    String id;

    @Column(nullable = false)
    String url;

    @Column
    Boolean isMainPageActive;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adv_content_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
    AdvContent advContent;

    @Column
    Date creatingDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    Manager manager;

}
