package com.A108.Watchme.VO.Entity.room;

import com.A108.Watchme.VO.ENUM.RoomStatus;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.group.GroupCategory;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long id;

    @Column(name = "room_name", length = 45)
    private String roomName;


    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private Integer view;

    @OneToOne(mappedBy = "room")
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private Member member;

    @OneToOne(mappedBy = "room")
    @JsonManagedReference
    private RoomInfo roomInfo;

    @OneToOne
    @JoinColumn(name = "room_ctg")
    private Category roomCtg;


}
