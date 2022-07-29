package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {


    Page<Room> findAllByOrderByViewDesc(Pageable pageable);


}
