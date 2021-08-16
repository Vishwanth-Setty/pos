package com.increff.pos.controller;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() {
        List<OrderData> orderDataList = new ArrayList<>();
        List<OrderPojo> orderPojoList = orderService.getAll();
        for(OrderPojo orderPojo: orderPojoList ){
            orderDataList.add(ConvertUtil.convert(orderPojo));
        }
        return orderDataList;
    }

    @ApiOperation(value = "Get Order by Id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getOrderById(@PathVariable int id) {
        List<OrderItemPojo> orderItemPojoList = orderService.getById(id);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            String barcode = productService.getProduct(orderItemPojo.getProductId()).getBarcode();
            OrderItemData orderItemData =ConvertUtil.convert(orderItemPojo);
            orderItemData.setBarcode(barcode);
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }

    @ApiOperation(value = "Create Order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void createOrder(@RequestBody OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemList();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemFormList){
            orderItemPojoList.add(ConvertUtil.convert(orderItemForm));
        }
        orderService.create(orderItemPojoList);
    }

    @ApiOperation(value = "Update Order")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void updateOrder(@RequestBody OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemList();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemFormList){
            orderItemPojoList.add(ConvertUtil.convert(orderItemForm));
        }
        orderService.update(orderItemPojoList);
    }


}
