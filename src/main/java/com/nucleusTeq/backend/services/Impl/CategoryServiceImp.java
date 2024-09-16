package com.nucleusTeq.backend.services.Impl;


import com.nucleusTeq.backend.dto.CategoryDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.exception.DataIntegrityViolationCustomException;
import com.nucleusTeq.backend.exception.MethodNotFoundException;
import com.nucleusTeq.backend.exception.ResourceConflictException;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.CategoryMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.services.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements ICategoryService {


    private final CategoryRepository categoryRepository;


    private final BooksRepository booksRepository;


    private final IssuanceRepository issuanceRepository;

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
    public String saveCategories(CategoryDTO categoryDTO) {

        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new ResourceConflictException("Category already exists with name: " + categoryDTO.getName());
        }


        Category categoryToSave = CategoryMapper.maptoCategory(categoryDTO);


        categoryRepository.save(categoryToSave);

        return "Category is added";
    }

    @Transactional
    @Override
    public String deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category not found ");
        }


        List<Books> booksInCategory = booksRepository.findByCategoryId(id);

        if (booksInCategory.isEmpty()) {
            categoryRepository.deleteById(id);
            return "Category deleted successfully";
        }

        boolean hasIssuedBooks = booksInCategory.stream()
                .anyMatch(book -> issuanceRepository.existsByBookIdAndStatus(book.getId(), "Issued"));

        if (hasIssuedBooks) {
            throw new MethodNotFoundException("Category cannot be deleted as some books under this category are currently issued.");
        }

        booksInCategory.forEach(book -> issuanceRepository.deleteByBookId(book.getId()));

        booksRepository.deleteAll(booksInCategory);


        categoryRepository.deleteById(id);

        return "Category and all related books deleted successfully";
    }


    @Override
    public Page<Category> getCategories(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (search != null && !search.isEmpty()) {

            return categoryRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {

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


            try {
                categoryRepository.save(existingCategory);
                return "Category updated successfully ";
            } catch (DataIntegrityViolationException ex) {

                throw new ResourceConflictException("Category with the same name already exists");
            }
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
