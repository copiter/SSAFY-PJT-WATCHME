package com.A108.Watchme.VO.Entity.sprint;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SprintInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long id;

    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "end_at")
    private Date endAt;

    @Column(name = "fee")
    private Integer fee;

    @Column(name="goal")
    private String goal;

    @Column(name = "sprint_img")
    private String img;

    @Column(name = "penalty_money")
    private Integer penaltyMoney;

}
