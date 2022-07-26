package com.A108.Watchme.VO.Entity.room;

import com.A108.Watchme.VO.Entity.Category;

import javax.persistence.*;

@Entity
public class RoomCtg {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ctg_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;
}
