package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.group.GroupCategory;
import com.A108.Watchme.VO.Entity.log.GroupApplyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupCategoryRepository extends JpaRepository<GroupCategory, Long> {

    List<GroupCategory> findAllByGroupId(Long groupId);

}
