package com.codes.study_core_service.category.service.Impl;

import com.codes.study_core_service.category.model.Category;
import com.codes.study_core_service.category.repository.CategoryRepository;
import com.codes.study_core_service.category.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public List<Category> listCategoriesActive(String userId) {
        return this.categoryRepository.findByUserIdAndActiveTrueOrderByNameAsc(userId);
    }

    @Override
    public List<Category> listCategoriesDesactive(String userId) {
        return this.categoryRepository.findByUserIdAndActiveFalseOrderByNameAsc(userId);
    }

    @Override
    public Category findById(String userId, Long id) {
        return this.categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(()-> new RuntimeException("Category no encontrado"));
    }

    @Override
    public Category save(String userId, Category category) {
        category.setUserId(userId);

        if (this.categoryRepository.existsByUserIdAndName(userId, category.getName())){
            throw new RuntimeException("Existe una categoria con el mismo nombre");
        }

        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(String userId, Long id, Category category) {
        Category current = this.findById(userId, id);

        if (category.getName() != null && !category.getName().equals(current.getName())) {
            if (this.categoryRepository.existsByUserIdAndName(userId, category.getName())){
                throw new RuntimeException("Existe una categoria con el mismo nombre");
            }
            current.setName(category.getName());
        }

        if (category.getColor() != null) current.setColor(category.getColor());
        if (category.getIcon() != null) current.setIcon(category.getIcon());
        if (category.getActive() != null) current.setActive(category.getActive());


        return this.categoryRepository.save(current);
    }

    @Override
    public Category desactive(String userId, Long id) {
        Category current = this.findById(userId, id);
        current.setActive(false);

        return this.categoryRepository.save(current);
    }

}
