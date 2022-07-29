package com.A108.Watchme.VO.Entity.log;

import com.A108.Watchme.VO.Entity.Rule;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
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
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @ManyToOne
    @JoinColumn(name = "sr_id")
    private Sprint sprint;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "penalty_score")
    private Integer penaltyScore;

}
