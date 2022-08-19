package com.A108.Watchme.VO.Entity;

import com.A108.Watchme.VO.ENUM.CategoryList;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ctg_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ctg_name", length = 45)
    private CategoryList name;

}
