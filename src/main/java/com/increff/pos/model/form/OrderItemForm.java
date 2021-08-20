package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private int orderItemId;
    private int orderId;
    private String barcode;
    private int quantity;
    private double sellingPrice;
}
