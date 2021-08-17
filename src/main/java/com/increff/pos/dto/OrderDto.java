package com.increff.pos.dto;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDto {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;
    public List<OrderData> getAll(){
        List<OrderData> orderDataList = new ArrayList<>();
        List<OrderPojo> orderPojoList = orderService.getAll();
        for(OrderPojo orderPojo: orderPojoList ){
            orderDataList.add(ConvertUtil.convert(orderPojo));
        }
        return orderDataList;
    }
    public List<OrderItemData> getById(int id){
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
    public void add(OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemList();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemFormList){
            ProductPojo productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
            orderItemPojoList.add(ConvertUtil.convert(orderItemForm,productPojo.getId()));
        }
        orderService.create(orderItemPojoList);
    }
    public void update(OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemList();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemFormList){
            ProductPojo productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
            orderItemPojoList.add(ConvertUtil.convert(orderItemForm,productPojo.getId()));
        }
        orderService.update(orderItemPojoList);
    }
}
