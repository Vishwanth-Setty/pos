package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "OrderItemData")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemXMLs {

    @XmlElement(name = "OrderItem")
    private List<OrderItemXml> orderItemXmlList = null;
    @XmlElement(name = "date")
    private String date;
    @XmlElement(name = "orderId")
    private Integer orderId;
    @XmlElement(name = "totalQuantity")
    private Integer totalQuantity;
    @XmlElement(name = "totalAmount")
    private Double totalAmount;

}