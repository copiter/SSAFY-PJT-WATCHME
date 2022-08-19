package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findAllByGroupId(Long groupId);

    Optional<Sprint> findByGroupIdAndStatus(Long groupId, Status yes);

    List<Sprint> findAllByStatus(Status status);
}
