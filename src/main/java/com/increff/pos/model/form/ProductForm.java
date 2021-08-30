package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductForm {

    @NotBlank(message = "Can not be blank")
    private String barcode;

    @NotBlank(message = "Can not be blank")
    private String name;

    @NotBlank(message = "Can not be blank")
    private String brand;

    @NotBlank(message = "Can not be blank")
    private String category;

    @NotNull(message = "Can not contain non-numeric or empty")
    @Min(value = 0L, message = "The value must be positive")
    private Double mrp;
}
