package com.nucleusTeq.backend.dto;


import com.nucleusTeq.backend.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BooksDTO {

    private Long id;
    private String title;
    private String author ;

    @NotNull(message = "CategoryID name cannot be Empty")
    private Long categoryId;

    private Integer quantity;




}
