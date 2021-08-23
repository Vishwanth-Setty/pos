package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pdfGenerator.PdfGenerator;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Api
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    OrderDto orderDto;

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "Get Order by Id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getOrderById(@PathVariable int id) {
       return orderDto.getById(id);
    }

    @ApiOperation(value = "Create Order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void createOrder(@RequestBody OrderForm orderForm) throws ApiException {
        orderDto.add(orderForm);
    }

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path = "/invoice/{orderId}", method = RequestMethod.PUT)
    public void generateInvoice(@PathVariable int orderId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("xml");
        File xml = new File("./src/main/java/com/increff/pos/pdfGenerator/brand.xml");
        File xsl = new File("./src/main/java/com/increff/pos/pdfGenerator/brand.xsl");
        StreamSource streamSource = new StreamSource(xsl);
        System.out.println(xml.exists());
        System.out.println(xsl.exists());

        byte[] bis = PdfGenerator.generatePDF(xml,streamSource);
        System.out.println(bis);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "inline; filename=report.pdf");

        response.setContentType("application/pdf");
        response.getOutputStream().write(bis);
//        return ok(bis);

//        orderDto.generateInvoice(orderId);
    }

    @ApiOperation(value = "Update Order")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void updateOrder(@RequestBody OrderForm orderForm) throws ApiException {
        orderDto.update(orderForm);
    }

}
