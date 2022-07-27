package com.A108.Watchme.VO;

import lombok.*;

import javax.persistence.*;

import java.util.Date;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
