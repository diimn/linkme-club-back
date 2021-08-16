package org.feelfee.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "client_response",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"clientPhone", "clientName", "advSourceLink"})})
public class ClientResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    String id;

    @Column
    String clientName;

    @Column
    String clientPhone;

    @Column
    Boolean isRealtor;

    @Column
    String advShortLink;

    @Column
    String advSourceLink;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "advSourceLink", referencedColumnName = "url", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    Adv adv;


}
