package com.A108.Watchme.VO.Entity.group;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.Entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class GroupInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupInfo_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    private String imageLink;

    private String description;

    private Integer currMember;

    private Integer maxMember;

}
