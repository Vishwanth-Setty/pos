package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;

@Getter
@Setter
public class InventoryForm {
    @NotBlank
    private String barcode;
    private int quantity;
}
