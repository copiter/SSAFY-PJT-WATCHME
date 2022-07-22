package com.A108.Watchme.VO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id @GeneratedValue
    @Column(name="refresh_id")
    private Long id;
    private String token;
    private Date expiredAt;
}
