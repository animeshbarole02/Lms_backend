package com.nucleusTeq.backend.dto;
import lombok.Data;
import javax.validation.constraints.NotNull;


@Data
public class BooksDTO {

    private Long id;
    private String title;
    private String author ;

    @NotNull(message = "CategoryID name cannot be Empty")
    private Long categoryId;

    private Integer quantity;




}
