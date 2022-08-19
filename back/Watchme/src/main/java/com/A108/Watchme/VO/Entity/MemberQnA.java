package com.A108.Watchme.VO.Entity;

import com.A108.Watchme.VO.Entity.member.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MemberQnA {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "questioner_id")
    private Member questioner;

    @ManyToOne
    @JoinColumn(name = "answerer_id")
    private Member answerer;

    @Column(name = "qna_title")
    private String title;

//    private Status status;

    @Column(name = "qun_text", columnDefinition = "TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

}
