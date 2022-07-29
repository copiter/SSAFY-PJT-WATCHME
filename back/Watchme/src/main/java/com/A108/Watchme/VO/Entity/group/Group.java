package com.A108.Watchme.VO.Entity.group;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Groupss")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder

public class Group {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private Long id;

    @Column(name = "group_name", length = 45)
    private String groupName;

//    @ManyToOne
//    @JoinColumn(name="leader_id")
//    private Member leader;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    List<Sprint> sprints = new ArrayList<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MemberGroup> memberGroupList = new ArrayList<>();

    @OneToOne(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonManagedReference
    private GroupInfo groupInfo;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GroupCategory> category;

    private Integer view;
}
