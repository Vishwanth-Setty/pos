package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.ReportForm;
import com.increff.pos.pojo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ReportServiceTest extends AbstractUnitTest {

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
    ReportService reportService;

    @Test
    public void testGetReport() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),10,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        orderService.generateInvoice(orderPojo.getId());
        List<BrandPojo> brandPojoList = brandService.getAllBrands();
        List<ProductPojo> productPojoList = productService.getAll();
        ReportForm reportForm = create("12/12/2019","12/12/2022","new","cat");
        List<SalesReportData> salesReportDataList = reportService.getSalesReport(reportForm,brandPojoList,
                productPojoList,orderItemPojoList);

        assertEquals(salesReportDataList.get(0).getRevenue(),1000.00,DELTA);
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
