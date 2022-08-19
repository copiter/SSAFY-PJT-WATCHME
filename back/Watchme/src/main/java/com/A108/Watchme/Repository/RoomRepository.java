package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {


    Page<Room> findAllByStatusOrderByViewDesc(Pageable pageable, Status status);

    Page<Room> findAllByRoomCtgAndStatus(Category roomCtg, Pageable pageable, Status status);

    Page<Room> findAllByStatusAndRoomNameContaining(Status status, String title, Pageable pageable);

    Page<Room> findAllByRoomCtgAndStatusAndRoomNameContaining(Category roomCtg, Status status,  String title, Pageable pageable);

    List<Room> findAllBySprintIn(List<Sprint> sprints);
}
