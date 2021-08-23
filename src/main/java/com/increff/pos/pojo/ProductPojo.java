package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"barcode"})
)
public class ProductPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "productGen")
    @TableGenerator(
            name = "productGen",
            table = "seq_product",
            initialValue = 1,
            allocationSize = 1
    )
    private int id;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private int brandId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double mrp;

}
