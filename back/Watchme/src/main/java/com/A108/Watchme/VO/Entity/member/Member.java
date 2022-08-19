package com.A108.Watchme.VO.Entity.member;

import com.A108.Watchme.VO.ENUM.ProviderType;
import com.A108.Watchme.VO.ENUM.Role;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.Builder.Default;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name="nick_name", nullable = false)
    private String nickName;
    @Column(nullable = false)
    private String pwd;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToOne(mappedBy = "member")
    @JsonManagedReference
    private MemberInfo memberInfo;

//    @OneToMany(mappedBy = "leader")
//    private List<Group> leadList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<MemberGroup> memberGroupList;
}
