package com.A108.Watchme.VO.Entity.room;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfo {
    @Id
    @Column(name = "room_id", nullable=false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name="room_id")
    @JsonBackReference
    private Room room;
    private Integer pwd;
    private String description;

    private Integer currMember;
    private Timestamp createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endAt;

    private Integer maxMember;

    @Column(name="img_link")
    private String imageLink;


}
