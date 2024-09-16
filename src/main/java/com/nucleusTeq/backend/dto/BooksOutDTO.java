package com.nucleusTeq.backend.dto;

import com.nucleusTeq.backend.entities.Category;
import lombok.Data;


import javax.validation.constraints.NotNull;


@Data

public class BooksOutDTO {

    private Long id;
    private String title;
    private String author ;

    @NotNull(message = "CategoryID name cannot be Empty")
    private Category category;

    private Integer quantity;

}
