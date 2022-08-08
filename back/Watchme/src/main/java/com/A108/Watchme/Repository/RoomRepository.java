package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {


    Page<Room> findAllByOrderByViewDesc(Pageable pageable);

    Page<Room> findAllByRoomCtg(Category roomCtg, Pageable pageable);

    Page<Room> findAllByRoomNameContaining(String title, Pageable pageable);

    Page<Room> findAllByRoomCtgAndRoomNameContaining(Category roomCtg, String title, Pageable pageable);
}
