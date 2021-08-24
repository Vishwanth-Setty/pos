package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderForm {
    private Integer id;
    private List<OrderItemForm> orderItemList;
}
