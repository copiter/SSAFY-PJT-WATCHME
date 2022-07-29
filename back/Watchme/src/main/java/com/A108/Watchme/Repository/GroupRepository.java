package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Page<Group> findAllByOrderByViewDesc(Pageable pageable);

//    List<Group> findAllGroupByMember_id(Long id);

//    List<Group> findAllByIdIn(List<Long> groupIds);


}
