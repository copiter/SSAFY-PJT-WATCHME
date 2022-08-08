package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.group.GroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {

}
