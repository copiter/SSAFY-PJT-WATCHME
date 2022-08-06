package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.group.GroupCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupCategoryRepository extends JpaRepository<GroupCategory, Long> {
}
