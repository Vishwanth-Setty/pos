package com.increff.pos.dto;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.data.OrderItemXMLs;
import com.increff.pos.model.data.OrderItemXml;
import com.increff.pos.pdfGenerator.PdfGenerator;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class InvoiceHelper {

    @Autowired
    OrderDto orderDto;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;


    public String downloadInvoice(int orderId) throws Exception {
        File myObj = new File("./src/main/java/com/increff/pos/pdfGenerator/invoice.xml");

        List<OrderItemData> orderItemDataList = orderDto.getById(orderId);
        List<OrderItemXml> orderItemXmlList = new ArrayList<>();
        for(OrderItemData orderItemData: orderItemDataList){
            ProductPojo productPojo = productService.getByBarcode(orderItemData.getBarcode());
            OrderItemXml orderItemXml = ConvertUtil.covert(orderItemData,productPojo.getMrp());
            orderItemXmlList.add(orderItemXml);
        }

        OrderPojo orderPojo = orderService.getOnlyOrderById(orderId);
        OrderItemXMLs orderItemsXMLs = new OrderItemXMLs();
        orderItemsXMLs.setOrderItemXmlList(orderItemXmlList);
        Integer totalQuantity = 0;
        Double totalAmount = 0.00;
        for(OrderItemXml orderItemXml : orderItemXmlList){
            totalQuantity+=orderItemXml.getQuantity();
            totalAmount+=orderItemXml.getTotalSellingPrice();
        }
        orderItemsXMLs.setTotalAmount(totalAmount);
        orderItemsXMLs.setTotalQuantity(totalQuantity);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = orderPojo.getOrderTime().toLocalDateTime();
        String formattedDateTime = dateTime.format(formatter);


        orderItemsXMLs.setOrderId(orderId);
        orderItemsXMLs.setDate(formattedDateTime);
        PdfGenerator.generateXml(myObj,orderItemsXMLs,OrderItemXMLs.class);

        File xml = new File("./src/main/java/com/increff/pos/pdfGenerator/invoice.xml");
        File xsl = new File("./src/main/java/com/increff/pos/pdfGenerator/invoice.xsl");
        StreamSource streamSource = new StreamSource(xsl);

        byte[] bis = PdfGenerator.generatePDF(xml,streamSource);
        String output = Base64.getEncoder().encodeToString(bis);

        return output;
    }
}
