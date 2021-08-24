package com.increff.pos.service;

import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.ReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.increff.pos.utils.ConvertUtil.convert;

@Service
public class ReportService extends AbstractApi {

    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Transactional(readOnly = true)
    public List<SalesReportData> getSalesReport(ReportForm reportForm) throws ApiException {
        checkValid(reportForm);

        LocalDate localDate = LocalDate.of(2019, 03, 12);
        LocalTime localTime = LocalTime.of(12,  44);
        ZoneId zoneId = ZoneId.of("GMT+05:30");
        ZonedDateTime startDate = convert((reportForm.getStartDate().split("/")),"start");
        ZonedDateTime endDate = convert(reportForm.getEndDate().split("/"),"end");
        if(startDate.isAfter(endDate)){
            throw new ApiException("Check Dates");
        }
        ZonedDateTime timeStamp = ZonedDateTime.of( localDate, localTime, zoneId );

        List<BrandPojo> brandPojoList = brandService.getAllBrands();
        HashMap<Integer,String> brandIdToNameAndCategory=new HashMap<>();
        brandListToMap(brandPojoList,brandIdToNameAndCategory);

        String filterBrand = reportForm.getBrand();
        String filterCategory =  reportForm.getCategory();

        List<ProductPojo> productPojoList = productService.getAll();
        HashMap<Integer,String> mapProductIdWithBrand=new HashMap<Integer,String>();
        productListToMap(productPojoList,mapProductIdWithBrand,brandIdToNameAndCategory);

        List<OrderPojo> orderPojoList = orderService.getAllInvoiceGeneratedOrder(true);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderPojo orderPojo:orderPojoList){
            orderItemPojoList.addAll(orderService.getById(orderPojo.getId()));
        }

        HashMap<String,List<OrderItemPojo>> uniqueCategories = new HashMap<>();
        for(OrderItemPojo orderItemPojo:orderItemPojoList){
            OrderPojo orderPojo = orderService.getOnlyOrderById(orderItemPojo.getOrderId());
            String brandAndCategory = mapProductIdWithBrand.get(orderItemPojo.getProductId());
            String brand = brandAndCategory.split("#")[0];
            String category = brandAndCategory.split("#")[1];
            if(!filterBrand.equals("")){
                if(!filterBrand.equals(brand)){
                    continue;
                }
            }
            if(!filterCategory.equals("")){
                if(!filterCategory.equals(category)) {
                    continue;
                }
            }
            if(!(startDate.isBefore(orderPojo.getOrderTime()) && endDate.isAfter(orderPojo.getOrderTime()))){
                continue;
            }

            if(!uniqueCategories.containsKey(category)){
                List<OrderItemPojo> tempOrderItemPojoList = new ArrayList<>();
                tempOrderItemPojoList.add(orderItemPojo);
                uniqueCategories.put(category,tempOrderItemPojoList);
            }
            else{
                List<OrderItemPojo> tempOrderItemPojoList = uniqueCategories.get(category);
                tempOrderItemPojoList.add(orderItemPojo);
                uniqueCategories.replace(category,tempOrderItemPojoList);
            }

        }

        List<SalesReportData> salesReportDataList = new ArrayList<>();
        for (Map.Entry<String,List<OrderItemPojo>> entry : uniqueCategories.entrySet()){
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setRevenue(0.00);
            salesReportData.setQuantity(0);
            salesReportData.setCategory(entry.getKey());
            for(OrderItemPojo orderItemPojo:entry.getValue()){
                salesReportData.setQuantity(salesReportData.getQuantity()+orderItemPojo.getQuantity());
                salesReportData.setRevenue(salesReportData.getRevenue()+orderItemPojo.getQuantity()*orderItemPojo.getSellingPrice());
            }
            salesReportDataList.add(salesReportData);
        }

        return salesReportDataList;
    }

    //Private Classes

    private void brandListToMap(List<BrandPojo> brandPojoList,HashMap<Integer,String> brandIdToNameAndCategory){
        for(BrandPojo brandPojo:brandPojoList){
            String brand = brandPojo.getBrand();
            String category = brandPojo.getCategory();
            brandIdToNameAndCategory.put(brandPojo.getId(),brand+'#'+category);
        }
    }

    private void productListToMap(List<ProductPojo> productPojoList,
                                  HashMap<Integer,String> mapProductIdWithBrand,
                                  HashMap<Integer,String> brandIdToNameAndCategory){
        for(ProductPojo productPojo:productPojoList){
            mapProductIdWithBrand.put(productPojo.getId(),brandIdToNameAndCategory.get(productPojo.getBrandId()));
        }

    }
}
