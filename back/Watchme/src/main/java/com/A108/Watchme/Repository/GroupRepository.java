package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Page<Group> findAllByOrderByViewDesc(Pageable pageable);
    Page<Group> findAllByStatusOrderByViewDesc(Status status, Pageable pageable);

//    Page<Group> findAllByDisplayOrderByViewDesc(int display, Pageable pageable);

    Page<Group> findAllByCategory_categoryAndStatus(Category category,Status status, Pageable pageable);

//    Page<Group> findAllByCategory_categoryAndDisplay(Category category, int display,Pageable pageable);

    Page<Group> findAllByCategory_categoryAndStatusAndGroupNameContaining(Category category, Status status, String keyword, Pageable pageable);

//    Page<Group> findAllByCategory_categoryAndDisplayAndGroupNameContaining(Category category, int display,String keyword, Pageable pageable);

    Page<Group> findAllByStatusAndGroupNameContaining(Status status, String keyword, Pageable pageable);

//    Page<Group> findAllByGroupNameContainingAndDisplay(String keyword, int display, Pageable pageable);

}
