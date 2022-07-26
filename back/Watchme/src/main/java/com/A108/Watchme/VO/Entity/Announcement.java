package com.A108.Watchme.VO.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter

public class Announcement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announce_id")
    private Long id;
    @Column(name = "announce_title")
    private String announceTitle;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name="annonce_text")
    private String announceText;
}
