package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService extends AbstractApi {

    @Autowired
    InventoryDao inventoryDao;

    @Transactional
    public List<InventoryPojo> getAll(){
        return inventoryDao.selectAll();
    }

    @Transactional
    public InventoryPojo getById(int id){
        return inventoryDao.select(id);
    }

    @Transactional
    public InventoryPojo add(InventoryPojo inventoryPojo) throws ApiException{
        InventoryPojo exists = inventoryDao.select(inventoryPojo.getProductId());
        checkNull(exists,"Inventory exists");
        return inventoryDao.persist(inventoryPojo);
    }

    @Transactional
    public void update(InventoryPojo inventoryPojo){
        InventoryPojo oldInventoryPojo = inventoryDao.select(inventoryPojo.getProductId());
        oldInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

}
