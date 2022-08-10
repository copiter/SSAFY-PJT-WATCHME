package com.A108.Watchme.VO.Entity;

import com.A108.Watchme.VO.ENUM.RuleName;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Rule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer id;

    @Column(name = "rule_name")
    private RuleName ruleName;

}
