package com.A108.Watchme.VO.Entity.room;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
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

    private String description;

    private Integer currMember;

    private Integer maxMember;

    @Column(name="img_link")
    private String imageLink;

}
