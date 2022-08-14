package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRoomLogRepositoy extends JpaRepository<MemberRoomLog, Long> {
    List<MemberRoomLog> findAllByRoomIdAndStatus(Long roomId, Status status);
}
