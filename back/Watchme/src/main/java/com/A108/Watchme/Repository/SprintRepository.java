package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findAllByGroupId(Long groupId);
}
