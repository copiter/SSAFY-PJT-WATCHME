package com.A108.Watchme.VO.Entity.log;
import com.A108.Watchme.VO.ENUM.Mode;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PenaltyLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_log_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name= "room_id")
    private Room room;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Mode mode;
}