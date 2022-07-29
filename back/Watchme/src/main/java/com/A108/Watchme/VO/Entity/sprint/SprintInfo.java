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

    @Column(name = "sprint_score")
    private Integer sprintScore;

    private Integer fee;

    @Column(name = "sprint_img")
    private String img;

}
