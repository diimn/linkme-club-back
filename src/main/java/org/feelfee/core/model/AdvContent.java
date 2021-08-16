package org.feelfee.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "adv_content")
@ToString
public class AdvContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="system-uuid" )
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    String id;

    @Column
    String bonus;

    @Column
    String headTop;

    @Column
    String price;

    @Column
    String price_comment;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String slider1_comments;

    @Column
    String descHead;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String description;

    @Column
    String brokerName;

    @Column
    String brokerComment;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String bullets;

    @Column
    String coordinates;

    @Column
    String shareText;

}
