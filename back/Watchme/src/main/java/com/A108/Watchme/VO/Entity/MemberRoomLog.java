package com.A108.Watchme.VO.Entity;

import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class MemberRoomLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_log_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private Timestamp start_at;

    @Column(name = "study_time")
    private Double studyTime;

}
