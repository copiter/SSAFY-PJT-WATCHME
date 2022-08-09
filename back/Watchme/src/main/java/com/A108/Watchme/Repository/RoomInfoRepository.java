package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {

    RoomInfo findByRoom(Room room);
}
