package com.A108.Watchme.VO.Entity;

import com.A108.Watchme.VO.ENUM.Status;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Groupss")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Group {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private Long id;

    @Column(name = "group_name", length = 45)
    private String groupName;

    @ManyToOne
    @JoinColumn(name="leader")
    private Member leader;

//    private Timestamp createdAt;
//    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "leader")
    List<Sprint> sprints = new ArrayList<>();
}
