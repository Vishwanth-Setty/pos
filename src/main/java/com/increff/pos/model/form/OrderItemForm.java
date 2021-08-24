package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderItemForm {
    private Integer orderItemId;
    @NotBlank
    private Integer orderId;
    @NotBlank
    private String barcode;
    @NotBlank
    private Integer quantity;
    @NotBlank
    private Double sellingPrice;
}
