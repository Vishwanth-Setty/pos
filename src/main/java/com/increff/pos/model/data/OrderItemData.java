package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public class OrderItemData {
    private Integer orderItemId;
    private Integer orderId;
    private String barcode;
    private Integer quantity;
    private Double sellingPrice;

}
