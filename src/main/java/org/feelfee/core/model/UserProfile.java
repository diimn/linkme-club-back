package org.feelfee.core.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "user_profile",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"socialId", "socialType"})})
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator ="system-uuid" )
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    String id;

    @Column
    String userName;

    @Column
    String userProfileImageLink;

    @Column
    String socialType;

    @Column
    String socialId;

    @Column
    String accessToken;


}
