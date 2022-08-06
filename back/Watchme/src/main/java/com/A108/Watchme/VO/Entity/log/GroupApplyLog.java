package com.A108.Watchme.VO.Entity.log;

import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.member.Member;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class GroupApplyLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_log_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apply_date")
    private Date apply_date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date update_date;

    // 0 : 보류 중
    // 1 : 허가
    // 2 : 거절
    // 4 : 취소
    // 5 : 탈퇴
    private int status;
}
