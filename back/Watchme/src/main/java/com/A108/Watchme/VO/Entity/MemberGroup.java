package com.A108.Watchme.VO.Entity;

import com.A108.Watchme.VO.ENUM.GroupRole;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.member.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mg_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    private GroupRole groupRole;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

}
