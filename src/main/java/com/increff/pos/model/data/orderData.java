package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class orderData {
    private int orderId;
    private ZonedDateTime orderTime;
    private boolean invoiceGenerated;
}
