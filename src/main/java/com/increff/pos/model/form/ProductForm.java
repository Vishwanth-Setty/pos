package com.increff.pos.model.form;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductForm {

    @NotBlank
    private String barcode;

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @NotNull
    @Min(value = 0L, message = "The value must be positive")
    private Double mrp;
}
