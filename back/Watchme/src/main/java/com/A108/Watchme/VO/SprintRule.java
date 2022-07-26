package com.A108.Watchme.VO;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
public class SprintRule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sr_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @Column(name = "rule_penalty")
    private Integer rulePenalty;

}
