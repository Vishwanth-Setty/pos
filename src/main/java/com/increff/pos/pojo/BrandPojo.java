package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"brand", "category"}))
@Getter
@Setter
public class BrandPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "brandGen")
    @TableGenerator(
            name = "brandGen",
            table = "seq_brand",
            initialValue = 1,
            allocationSize = 1
    )
    private int id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String category;
}
