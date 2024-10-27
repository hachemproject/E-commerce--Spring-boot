package com.beststore.beststore.service;
import org.springframework.data.jpa.repository.JpaRepository;


import com.beststore.beststore.models.Category;

public interface CategoryRepository extends JpaRepository<Category,  Integer> {

}
