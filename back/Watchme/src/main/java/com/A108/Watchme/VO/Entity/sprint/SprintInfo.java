package com.A108.Watchme.VO.Entity.sprint;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

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

    @Column(name = "routineStart_at")
    private Timestamp routineStartAt;

    @Column(name = "routineEnd_at")
    private Timestamp routineEndAt;

    @Column(name = "sprint_score")
    private Integer sprintScore;

    private String goal;

    private Integer fee;

    @Column(name = "sprint_img")
    private String img;

    private String description;
}
