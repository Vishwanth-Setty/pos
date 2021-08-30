package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class InventoryForm {
    @NotBlank(message = "Can not be blank")
    private String barcode;

    @NotNull(message = "Can not contain non-numeric or empty")
    @Pattern(regexp = "[\\s]*[0-9]*[1-9]+",message="Should be a positive integer")
    private Integer quantity;
}
