package com.codes.study_core_service.category.controller;

import com.codes.study_core_service.category.model.Category;
import com.codes.study_core_service.category.repository.CategoryRepository;
import com.codes.study_core_service.category.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/all")
    public List<Category> getAllCategories(){
        return this.categoryService.findAll();
    }

    @GetMapping("/allActives")
    public List<Category> getAllActives(@RequestHeader ("X-User-Id") String userId){
        return this.categoryService.listCategoriesActive(userId);
    }

    @GetMapping("/allDesactives")
    public List<Category> getAllDesactives(@RequestHeader ("X-User-Id") String userId){
        return this.categoryService.listCategoriesDesactive(userId);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@RequestHeader("X-User-Id") String userId, @PathVariable Long id){
        return this.categoryService.findById(userId, id);
    }

    @PostMapping()
    public Category addCategory(@RequestHeader("X-User-Id") String userId, @RequestBody Category category){
        return this.categoryService.save(userId, category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@RequestHeader("X-User-Id") String userId,
                                   @PathVariable Long id,
                                   @RequestBody Category category){
        return this.categoryService.update(userId, id, category);
    }

    @PostMapping("/desactive/{id}")
    public Category desactivateCategory(@RequestHeader("X-User-Id") String userId, @PathVariable Long id){
        return this.categoryService.desactive(userId, id);
    }
}
