package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderItemForm {

    private Integer orderItemId;
    @NotBlank(message = "Can not be blank")
    private Integer orderId;
    @NotBlank(message = "Can not be blank")
    private String barcode;
    @NotBlank(message = "Can not be blank")
    private Integer quantity;
    @NotBlank(message = "Can not be blank")
    private Double sellingPrice;

}
