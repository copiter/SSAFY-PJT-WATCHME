package com.A108.Watchme.VO.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Sprint {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "sprint_name")
    private String name;

    @Column(name = "sum_point")
    private Integer sumPoint;

}
