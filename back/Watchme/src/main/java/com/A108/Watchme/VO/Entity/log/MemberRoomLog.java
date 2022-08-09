package com.A108.Watchme.VO.Entity.log;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "joined_at")
    private Date joinedAt;

    @Column(name="study_time")
    private Integer studyTime;

}
