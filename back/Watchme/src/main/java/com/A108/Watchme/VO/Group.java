package com.A108.Watchme.VO;

import lombok.*;

import javax.persistence.*;

@Entity(name = "Groupss")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Group {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private Long id;

    @Column(length = 45)
    private String group_name;

    @ManyToOne
    @JoinColumn(name="leader")
    private Member member;

//    private Timestamp createdAt;
//    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

}
