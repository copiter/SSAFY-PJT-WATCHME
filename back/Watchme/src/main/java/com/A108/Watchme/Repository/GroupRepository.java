package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Page<Group> findAllByOrderByViewDesc(Pageable pageable);

//    Page<Group> findAllByDisplayOrderByViewDesc(int display, Pageable pageable);

    Page<Group> findAllByCategory_category(Category category, Pageable pageable);

//    Page<Group> findAllByCategory_categoryAndDisplay(Category category, int display,Pageable pageable);

    Page<Group> findAllByCategory_categoryAndGroupNameContaining(Category category, String keyword, Pageable pageable);

//    Page<Group> findAllByCategory_categoryAndDisplayAndGroupNameContaining(Category category, int display,String keyword, Pageable pageable);

    Page<Group> findAllByGroupNameContaining(String keyword, Pageable pageable);

//    Page<Group> findAllByGroupNameContainingAndDisplay(String keyword, int display, Pageable pageable);

}
