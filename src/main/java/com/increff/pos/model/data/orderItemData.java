package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class orderItemData {
    private int orderItemId;
    private int orderId;
    private String barcode;
    private int quantity;
    private double sellingPrice;

}
