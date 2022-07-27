package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Page<Group> findAllByOrderByViewDesc(Pageable pageable);

}
