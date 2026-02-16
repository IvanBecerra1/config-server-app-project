package com.codes.study_core_service.category.service;

import com.codes.study_core_service.category.model.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> findAll();

    List<Category> listCategoriesActive(String userId);

    List<Category> listCategoriesDesactive(String userId);

    Category findById(String userId, Long id);

    Category save(String userId, Category category);

    Category update(String userId, Long id, Category category);

    Category desactive(String userId, Long id);

}
