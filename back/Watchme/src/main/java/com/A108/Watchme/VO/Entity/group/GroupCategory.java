package com.A108.Watchme.VO.Entity.group;

import com.A108.Watchme.VO.Entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class GroupCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_ctg_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ctg_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;
}
