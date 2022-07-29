package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
