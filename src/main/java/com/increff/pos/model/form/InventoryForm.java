package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryForm {
    private int productId;
    private String barcode;
    private int quantity;
}
