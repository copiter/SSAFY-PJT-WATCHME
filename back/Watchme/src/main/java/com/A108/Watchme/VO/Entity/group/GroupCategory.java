package com.A108.Watchme.VO.Entity.group;

import com.A108.Watchme.VO.Entity.Category;

import javax.persistence.*;

@Entity
public class GroupCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gctg_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ctg_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
