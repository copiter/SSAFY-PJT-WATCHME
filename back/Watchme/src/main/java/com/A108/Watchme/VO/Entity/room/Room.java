package com.A108.Watchme.VO.Entity.room;

import com.A108.Watchme.VO.ENUM.RoomStatus;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import lombok.*;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name="owner_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToOne(mappedBy = "room")
    private Sprint sprint;

    private Integer view;

}
