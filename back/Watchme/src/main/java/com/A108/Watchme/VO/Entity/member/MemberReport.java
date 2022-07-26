package com.A108.Watchme.VO.Entity.member;

import com.A108.Watchme.VO.Entity.ReportCategory;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="report_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @ManyToOne
    @JoinColumn(name = "reportee_id")
    private Member reportee;

    @ManyToOne
    @JoinColumn(name = "rc_id")
    private ReportCategory reportCategory;

    @Column(name = "report_reason")
    private String reportReason;

}
