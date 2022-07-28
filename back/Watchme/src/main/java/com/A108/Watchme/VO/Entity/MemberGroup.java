package com.A108.Watchme.VO.Entity;

import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.member.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MemberGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mg_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

}
