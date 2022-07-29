package com.A108.Watchme.VO.Entity.room;

import com.A108.Watchme.VO.Entity.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class RoomCtg {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_ctg_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ctg_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
