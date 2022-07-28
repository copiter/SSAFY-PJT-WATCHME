package com.A108.Watchme.VO.Entity.member;

import com.A108.Watchme.VO.ENUM.Gender;
import lombok.*;

import javax.persistence.*;

import java.util.Date;


@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberInfo{
    @Id
    @Column(name="member_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name="member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birth;

    private String name;

    private int point;

    private int score;

    @Column(name="img_link")
    private String imageLink;

    @Column(name = "study_time")
    private Integer studyTime;

    @Column(name = "study_time_day")
    private Integer studyTimeDay;

    @Column(name = "study_time_week")
    private Integer studyTimeWeek;

    @Column(name = "study_time_month")
    private Integer studyTimeMonth;

}
