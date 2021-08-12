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
    private int productId;

    @Column(nullable = false)
    private int quantity;

}
