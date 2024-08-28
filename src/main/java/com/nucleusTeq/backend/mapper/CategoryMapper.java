package com.nucleusTeq.backend.mapper;

import com.nucleusTeq.backend.dto.CategoryDTO;
import com.nucleusTeq.backend.entities.Category;



public class CategoryMapper {

    private  CategoryMapper(){

    }


    public static CategoryDTO mapToCategoryDTO(Category category)
    {
           CategoryDTO categoryDTO = new CategoryDTO();
           categoryDTO.setId(category.getId());
           categoryDTO.setName(category.getName());
           categoryDTO.setCategoryDesc(category.getCategoryDesc());

           return  categoryDTO;
    }


    public  static  Category maptoCategory(CategoryDTO categoryDTO )
    {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        category.setCategoryDesc(categoryDTO.getCategoryDesc());

        return category;
    }



}
