package com.A108.Watchme.VO.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReportCategory {

    @Id
    @Column(name = "rc_id")
    private int id;

    @Column(name = "rc_name", length = 45)
    private String name;



}
