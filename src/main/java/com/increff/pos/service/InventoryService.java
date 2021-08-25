package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService extends AbstractApi {

    @Autowired
    InventoryDao inventoryDao;

    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    public InventoryPojo getById(int id) {
        return inventoryDao.selectByMethod("productId", id);
    }

    public InventoryPojo add(InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo exists = inventoryDao.selectByMethod("productId", inventoryPojo.getProductId());
        checkNull(exists, "Inventory already exists");
        return inventoryDao.persist(inventoryPojo);
    }

    public void update(InventoryPojo inventoryPojo) {
        InventoryPojo oldInventoryPojo = inventoryDao.selectByMethod("productId", inventoryPojo.getProductId());
        oldInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

}
