package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InventoryForm {
    @NotBlank
    private String barcode;

    @NotNull
    private Integer quantity;
}
