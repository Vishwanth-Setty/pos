package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@Setter
public class OrderPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "orderGen")
    @TableGenerator(
            name = "orderGen",
            table = "seq_order",
            initialValue = 1001,
            allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false)
    private ZonedDateTime orderTime;

    @Column(nullable = false)
    private Boolean invoiceGenerated;

}
