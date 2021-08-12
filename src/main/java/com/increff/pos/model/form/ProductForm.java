package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    private String barcode;
    private int brandId;
    private String name;
    private String brand;
    private String category;
    private double mrp;
}
