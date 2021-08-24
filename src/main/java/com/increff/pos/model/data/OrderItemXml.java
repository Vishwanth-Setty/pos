package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemXml {

    private String barcode;
    private Integer quantity;
    private Double mrp;
    private Double perUnitSellingPrice;
    private Double totalSellingPrice;

}
