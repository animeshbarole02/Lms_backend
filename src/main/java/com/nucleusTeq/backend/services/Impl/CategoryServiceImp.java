package com.nucleusTeq.backend.services.Impl;


import com.nucleusTeq.backend.dto.CategoryDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.CategoryMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.services.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImp implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private IssuanceRepository issuanceRepository;

    @Override
    public List<CategoryDTO> fetchCategories() {

        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        categories.forEach(category ->
                categoryDTOList.add(CategoryMapper.mapToCategoryDTO(category))
        );

        return categoryDTOList;

    }

    @Override

    public String saveCategories(List<CategoryDTO> categoryDTOList) {
        List<Category> categories = new ArrayList<>();

        categoryDTOList.forEach(categoryDTO -> {
            categories.add(CategoryMapper.maptoCategory(categoryDTO));
        });

        List<Category> savedCategories = categoryRepository.saveAll(categories);

        return savedCategories.size() + "Categories are added";
    }


    @Override
    public String deleteCategory(Long id) {

        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return "Category not found";
        }

        List<Books> booksInCategory = booksRepository.findByCategoryId(id);

        if (booksInCategory.isEmpty()) {
            categoryRepository.deleteById(id);
            return "Category deleted successfully";
        }



        boolean hasIssuedBooks = booksInCategory.stream()
                        .anyMatch(book ->issuanceRepository.existsByBookIdAndStatus(book.getId(),"Issued"));

        if (hasIssuedBooks) {

            return "Category cannot be deleted as some books under this category are currently issued.";
        }

        booksInCategory.forEach(book ->
                issuanceRepository.deleteByBookIdAndStatus(book.getId(), "Returned")
        );

        booksRepository.deleteAll(booksInCategory);
        categoryRepository.deleteById(id);

        return "Category and all related books deleted successfully with ID: " + id;
    }

    @Override
    public Page<Category> getCategories(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        if (search != null && !search.isEmpty()) {
            // If search term is present, filter categories by name
            return categoryRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            // Otherwise, return all categories with pagination
            return categoryRepository.findAll(pageable);
        }
    }

    @Override
    public Category getCategoryById(Long id) {

        Optional<Category> categoryOptional = categoryRepository.findById(id);


        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
    }

    @Override
    public String updateCategory(Long id,CategoryDTO categoryDTO) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isPresent()) {
            Category existingCategory  =  optionalCategory.get();
            existingCategory.setCategoryDesc(categoryDTO.getCategoryDesc());
            existingCategory.setName(categoryDTO.getName());

            categoryRepository.save(existingCategory);

            return  "Category updated successfully with ID: " + id;
        }else {
            throw  new ResourceNotFoundException("Book not fount with ID : "+ id);
        }

    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category != null) {
            return category;
        } else {
            throw new ResourceNotFoundException("Category not found with name: " + name);
        }
    }

    @Override
    public long getCategoryCount() {
        return categoryRepository.count();
    }


}
