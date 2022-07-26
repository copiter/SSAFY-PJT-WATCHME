package com.A108.Watchme.VO;

import com.A108.Watchme.VO.ENUM.RuleName;

import javax.persistence.*;

@Entity
public class Rule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer id;

    @Column(name = "rule_name")
    private RuleName ruleName;

}
