package com.codes.study_core_service.category.repository;

import com.codes.study_core_service.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    /**
     * Internamente se realiza la consulta hacia la BD
     * @param userId
     * @return
     */
    List<Category> findByUserIdAndActiveTrueOrderByNameAsc(String userId);

    List<Category> findByUserIdAndActiveFalseOrderByNameAsc(String userId);

    Optional<Category> findByIdAndUserId(Long id,  String userId);

    boolean existsByUserIdAndName(String userId, String nameCategory);

}
