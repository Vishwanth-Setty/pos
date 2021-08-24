package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
public class InventoryPojo {

    @Id
    private Integer productId;

    @Column(nullable = false)
    private Integer quantity;

}
