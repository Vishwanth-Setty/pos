package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(name = "uq_order_product", columnNames = {"orderId", "productId"})
)
@Getter
@Setter
public class OrderItemPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "orderItemGen")
    @TableGenerator(
            name = "orderItemGen",
            table = "seq_order_item",
            initialValue = 1,
            allocationSize = 1
    )
    private int id;

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private int productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double sellingPrice;

}
