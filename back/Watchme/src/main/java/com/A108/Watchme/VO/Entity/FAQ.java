package com.A108.Watchme.VO.Entity;

import javax.persistence.*;

@Entity
public class FAQ {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "faq_q")
    private String faqQ;

    @Column(name= "faq_A")
    private String faqA;


}
