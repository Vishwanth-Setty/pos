package com.increff.pos.controller;

import com.increff.pos.model.data.orderData;
import com.increff.pos.model.data.orderItemData;
import com.increff.pos.model.form.orderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<orderData> getAllOrders() {
        return null;
    }

    @ApiOperation(value = "Get Order by Id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<orderItemData> getOrderById() {
        return null;
    }

    @ApiOperation(value = "Create Order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public orderData createOrder(@RequestBody orderForm orderForm){
        return null;
    }

    @ApiOperation(value = "Update Order")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public orderData updateOrder(@RequestBody  orderForm orderForm) {
        return null;
    }

}
