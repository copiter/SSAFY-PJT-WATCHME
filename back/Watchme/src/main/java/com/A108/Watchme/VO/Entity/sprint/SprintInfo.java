package com.A108.Watchme.VO.Entity.sprint;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SprintInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long id;

    @OneToOne(mappedBy = "sprintInfo")
    private Sprint sprint;

    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "end_at")
    private Date endAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "routineStart_at")
    private Date routineStartAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "routineEnd_at")
    private Date routineEndAt;

    @Column(name = "sprint_score")
    private Integer sprintScore;

    private Integer fee;

    @Column(name="goal")
    private String goal;

    @Column(name = "sprint_img")
    private String img;

    @Column(name = "penalty_money")
    private Integer penaltyMoney;

    private String description;
}
