package com.praveenukkoji.productservice.service;

import com.praveenukkoji.productservice.dto.request.category.CreateCategoryRequest;
import com.praveenukkoji.productservice.dto.request.category.UpdateCategoryRequest;
import com.praveenukkoji.productservice.dto.response.category.CategoryResponse;
import com.praveenukkoji.productservice.exception.category.CategoryCreateException;
import com.praveenukkoji.productservice.exception.category.CategoryDeleteException;
import com.praveenukkoji.productservice.exception.category.CategoryNotFoundException;
import com.praveenukkoji.productservice.exception.category.CategoryUpdateException;
import com.praveenukkoji.productservice.model.Category;
import com.praveenukkoji.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // create
    public UUID createCategory(CreateCategoryRequest createCategoryRequest)
            throws CategoryCreateException {

        log.info("Creating new category: {}", createCategoryRequest);

        Category newCategory = Category.builder()
                .name(createCategoryRequest.getName())
                .build();

        try {
            return categoryRepository.save(newCategory).getId();
        } catch (DataIntegrityViolationException e) {
            throw new CategoryCreateException(e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new CategoryCreateException(e.getMessage());
        }
    }

    // retrieve
    public CategoryResponse getCategory(UUID categoryId)
            throws CategoryNotFoundException {

        log.info("Retrieving category: {}", categoryId);

        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isPresent()) {
            return CategoryResponse.builder()
                    .id(category.get().getId())
                    .name(category.get().getName())
                    .build();
        }

        throw new CategoryNotFoundException("category with id = " + categoryId + " not found");

    }

    // update
    public UUID updateCategory(UUID categoryId, UpdateCategoryRequest updateCategoryRequest)
            throws CategoryNotFoundException, CategoryUpdateException {

        log.info("Updating category: {}", updateCategoryRequest);

        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isPresent()) {
            Category updatedCategory = category.get();
            updatedCategory.setName(updateCategoryRequest.getName());

            try {
                return categoryRepository.save(updatedCategory).getId();
            } catch (Exception e) {
                throw new CategoryUpdateException(e.getMessage());
            }
        }

        throw new CategoryNotFoundException("category with id = " + categoryId + " not found");
    }

    // delete
    public void deleteCategory(UUID categoryId)
            throws CategoryNotFoundException, CategoryDeleteException {

        log.info("Deleting category: {}", categoryId);

        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isPresent()) {
            try {
                categoryRepository.deleteById(categoryId);
                return;
            } catch (Exception e) {
                throw new CategoryDeleteException(e.getMessage());
            }
        }

        throw new CategoryNotFoundException("category with id = " + categoryId + " not found");
    }

    // get all
    public List<CategoryResponse> getAllCategory() {

        log.info("Retrieving all categories");

        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
                .map(category -> {
                    return CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .build();
                })
                .toList();
    }
}


































