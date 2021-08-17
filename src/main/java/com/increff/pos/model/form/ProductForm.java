package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

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
    @NotBlank
    private double mrp;
}
