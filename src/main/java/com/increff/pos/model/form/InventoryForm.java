package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class InventoryForm {
    @NotBlank
    private String barcode;

    @NotNull
    @Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="Should be a positive integer")
    private Integer quantity;
}
