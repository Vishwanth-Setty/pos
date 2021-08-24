package com.increff.pos.controller;

import com.increff.pos.dto.InvoiceHelper;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    OrderDto orderDto;
    @Autowired
    InvoiceHelper invoiceHelper;

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "Get Order by Id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getById(@PathVariable int id) {
       return orderDto.getById(id);
    }

    @ApiOperation(value = "Create Order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void create(@RequestBody OrderForm orderForm) throws ApiException {
        orderDto.add(orderForm);
    }

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path = "/invoice/{orderId}", method = RequestMethod.PUT)
    public String generateInvoice(@PathVariable int orderId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        orderDto.generateInvoice(orderId);
        return invoiceHelper.downloadInvoice(orderId);
    }

    @ApiOperation(value = "Update Order")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void update(@RequestBody OrderForm orderForm) throws ApiException {
        orderDto.update(orderForm);
    }

}
