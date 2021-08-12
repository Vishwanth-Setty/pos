package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class orderForm {
    private int id;
    private List<orderItemForm> orderItemList;
}
