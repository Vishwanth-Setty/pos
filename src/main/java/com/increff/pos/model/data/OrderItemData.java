package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public class OrderItemData {
    private int orderItemId;
    private int orderId;
    private String barcode;
    private int quantity;
    private double sellingPrice;

}
