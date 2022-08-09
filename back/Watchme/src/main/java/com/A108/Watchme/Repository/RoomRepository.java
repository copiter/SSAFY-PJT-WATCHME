package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {


    Page<Room> findAllByRoomStatusOrderByViewDesc(Pageable pageable, Status status);

    Page<Room> findAllByRoomCtgAndRoomStatus(Category roomCtg, Pageable pageable, Status status);

    Page<Room> findAllByRoomStatusAndRoomNameContaining(String title, Pageable pageable, Status status);

    Page<Room> findAllByRoomCtgAndRoomStatusAndRoomNameContaining(Category roomCtg, String title, Pageable pageable, Status status);
}
