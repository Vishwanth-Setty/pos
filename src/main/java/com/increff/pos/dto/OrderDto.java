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
import com.increff.pos.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            String barcode = productService.getById(orderItemPojo.getProductId()).getBarcode();
            OrderItemData orderItemData =ConvertUtil.convert(orderItemPojo);
            orderItemData.setBarcode(barcode);
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }
    public void add(OrderForm orderForm) throws ApiException {
        checkDuplicatesRecords(orderForm.getOrderItemList());
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemList();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemFormList){
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            orderItemPojoList.add(ConvertUtil.convert(orderItemForm,productPojo.getId()));
        }
        orderService.create(orderItemPojoList);
    }
    public void update(OrderForm orderForm) throws ApiException {
        if(orderService.isInvoiceGenerated(orderForm.getId())){
            throw new ApiException("Invoice already generated");
        }
        checkDuplicatesRecords(orderForm.getOrderItemList());
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemList();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemFormList){
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            orderItemPojoList.add(ConvertUtil.convert(orderItemForm,productPojo.getId()));
        }
        orderService.update(orderItemPojoList);
    }

    public void generateInvoice(int orderId) throws ApiException{
        orderService.generateInvoice(orderId);
    }
    private void checkDuplicatesRecords(List<OrderItemForm> orderItemFormList) throws ApiException {
        Set<String> hash_Set = new HashSet<String>();
        for(OrderItemForm orderItemForm : orderItemFormList){
            if(hash_Set.contains(orderItemForm.getBarcode())){
                throw new ApiException("Found same barcode in multiple rows");
            }
            hash_Set.add(orderItemForm.getBarcode());
        }
    }
}
