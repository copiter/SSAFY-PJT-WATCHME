package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.sprint.SprintInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SprintInfoRepository extends JpaRepository<SprintInfo, Long> {

}
