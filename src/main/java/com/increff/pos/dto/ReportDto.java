package com.increff.pos.dto;

import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.ReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.utils.ConvertUtil.convert;

@Service
public class ReportDto extends AbstractApi {

    @Autowired
    ReportService reportService;

    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    public List<SalesReportData> getSalesReport(ReportForm reportForm) throws ApiException {
        checkValid(reportForm);
        ZonedDateTime startDate = convert((reportForm.getStartDate().split("/")),"start");
        ZonedDateTime endDate = convert(reportForm.getEndDate().split("/"),"end");
        if(startDate.isAfter(endDate)){
            throw new ApiException("Invalid date range provided");
        }
        List<BrandPojo> brandPojoList = brandService.getAllBrands();
        List<ProductPojo> productPojoList = productService.getAll();
        List<OrderPojo> orderPojoList = orderService.getAllInvoiceGeneratedOrder(true);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderPojo orderPojo:orderPojoList){
            if(!(startDate.isBefore(orderPojo.getOrderTime()) && endDate.isAfter(orderPojo.getOrderTime()))){
                continue;
            }
            orderItemPojoList.addAll(orderService.getById(orderPojo.getId()));
        }
        return reportService.getSalesReport(reportForm,brandPojoList,productPojoList,orderItemPojoList);
    }
}
