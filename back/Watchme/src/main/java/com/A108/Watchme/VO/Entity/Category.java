package com.A108.Watchme.VO.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Category {

    @Id @GeneratedValue
    @Column(name = "ctg_id")
    private int id;

    @Column(name = "ctg_name", length = 45)
    String name;

}
