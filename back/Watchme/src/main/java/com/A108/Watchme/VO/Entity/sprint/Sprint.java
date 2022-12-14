package com.A108.Watchme.VO.Entity.sprint;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.log.PointLog;
import com.A108.Watchme.VO.Entity.room.Room;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "sprint", fetch = FetchType.EAGER)
    @JsonManagedReference
    private SprintInfo sprintInfo;

    @OneToMany(mappedBy = "sprint")
    List<PointLog> pointLogList = new ArrayList<>();

}
