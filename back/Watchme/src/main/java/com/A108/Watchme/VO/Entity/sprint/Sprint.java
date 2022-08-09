package com.A108.Watchme.VO.Entity.sprint;

import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.log.PointLog;
import com.A108.Watchme.VO.Entity.room.Room;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name="room_id")
    private Room room;

    @Column(name = "sprint_name")
    private String name;

    @Column(name = "sum_point")
    private Integer sumPoint;

    @OneToMany(mappedBy = "sprint")
    List<PointLog> pointLogList = new ArrayList<>();

}
