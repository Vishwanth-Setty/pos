package com.increff.pos.controller;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderPojo> getAllOrders() {
        return orderService.getAll();
    }

    @ApiOperation(value = "Get Order by Id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getOrderById(@PathVariable int id) {
        return orderService.getById(id);
    }

    @ApiOperation(value = "Create Order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void createOrder(@RequestBody OrderForm orderForm) throws ApiException {
        orderService.create(orderForm);
    }

    @ApiOperation(value = "Update Order")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void updateOrder(@RequestBody OrderForm orderForm) throws ApiException {
        orderService.update(orderForm);
    }


}
