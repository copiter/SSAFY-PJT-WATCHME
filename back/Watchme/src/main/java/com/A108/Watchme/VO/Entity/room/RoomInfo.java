package com.A108.Watchme.VO.Entity.room;

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
    private Room room;

    private int num;

    private int score;

    private int diplsy;

    @Column(name="img_link")
    private String imageLink;
}
