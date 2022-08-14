package com.A108.Watchme.VO.Entity.sprint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SprintInfo {

    @Id
    @Column(name = "sprint_id")
    private Long id;

    @MapsId
    @JoinColumn(name = "sprint_id")
    @OneToOne
    @JsonBackReference
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
