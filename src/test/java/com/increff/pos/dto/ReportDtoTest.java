package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.ReportForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ReportDtoTest extends AbstractUnitTest {

    private static final double DELTA = 1e-15;

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    OrderService orderService;

    @Autowired
    ReportDto reportDto;

    @Test
    public void testGetReport() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),10,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        orderService.generateInvoice(orderPojo.getId());

        ReportForm reportForm = create("12/12/2019","12/12/2022","new","cat");
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(reportForm);

        assertEquals(salesReportDataList.get(0).getRevenue(),1000.00,DELTA);
    }

    @Test
    public void testGetReportWithNoFilters() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),10,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        orderService.generateInvoice(orderPojo.getId());

        ReportForm reportForm = create("12/12/2019","12/12/2022","","");
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(reportForm);

        assertEquals(salesReportDataList.get(0).getRevenue(),1000.00,DELTA);
    }

    @Test
    public void testGetReportWithFilters() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),10,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        orderService.generateInvoice(orderPojo.getId());

        ReportForm reportForm = create("12/12/2019","12/12/2022","nw","ad");
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(reportForm);

        assertEquals(salesReportDataList.size(),0);
    }

    @Test
    public void testCheckDates() {
        try{

            BrandPojo brandPojo = create("new","cat");
            ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
            InventoryPojo inventoryPojo = create(productPojo.getId(),100);
            List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
            orderItemPojoList.add(create(productPojo.getId(),10,100.00));
            OrderPojo orderPojo = orderService.create(orderItemPojoList);
            orderService.generateInvoice(orderPojo.getId());

            ReportForm reportForm = create("12/12/2019","12/12/2000","new","cat");
            List<SalesReportData> salesReportDataList = reportDto.getSalesReport(reportForm);
        }
        catch (ApiException apiException){
            assertEquals(apiException.getMessage(),"Invalid date range provided");
        }
    }
    @Test
    public void testCheckNonSelect() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),10,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        orderService.generateInvoice(orderPojo.getId());

        ReportForm reportForm = create("12/12/2019","17/08/2020","new","cat");
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(reportForm);
        assertEquals(salesReportDataList.size(),0);

    }
    private ProductPojo create(String barcode, int brandId, String name, Double mrp) throws ApiException {
        ProductPojo productPojo = new ProductPojo();

        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setName(name);
        productPojo.setBrandId(brandId);

        return productService.add(productPojo);
    }

    private BrandPojo create(String brand, String category) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();

        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        brandService.addBrand(brandPojo);

        return brandService.getBrandByNameAndCategory(brandPojo.getBrand(),brandPojo.getCategory());
    }

    private InventoryPojo create(int id, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();

        inventoryPojo.setQuantity(quantity);
        inventoryPojo.setProductId(id);

        return inventoryService.add(inventoryPojo);
    }

    private OrderItemPojo create(int productId,int quantity,double sellingPrice){
        OrderItemPojo orderItemPojo = new OrderItemPojo();

        orderItemPojo.setSellingPrice(sellingPrice);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setProductId(productId);

        return orderItemPojo;
    }

    private ReportForm create(String startDate,String endDate, String brand, String category){
        ReportForm reportForm = new ReportForm();

        reportForm.setBrand(brand);
        reportForm.setCategory(category);
        reportForm.setEndDate(endDate);
        reportForm.setStartDate(startDate);

        return reportForm;
    }
}
