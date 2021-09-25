package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;

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
    private Integer id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String category;
}
