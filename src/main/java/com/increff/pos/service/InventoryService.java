package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    InventoryDao inventoryDao;

    @Autowired
    ProductService productService;

    @Transactional
    public List<InventoryData> getInventoryList(){
        List<InventoryPojo> listInventoryPojo = inventoryDao.selectAll();
        List<InventoryData> listInventoryData = new ArrayList<InventoryData>();
        for(InventoryPojo inventoryPojo:listInventoryPojo){
            ProductData productData = productService.getProduct(inventoryPojo.getProductId());
            InventoryData tempInventoryData = new InventoryData();
            tempInventoryData.setBarcode(productData.getBarcode());
            tempInventoryData.setProductName(productData.getName());
            tempInventoryData.setProductId(productData.getId());
            tempInventoryData.setQuantity(inventoryPojo.getQuantity());
            listInventoryData.add(tempInventoryData);
        }
        return listInventoryData;
    }

    @Transactional
    public InventoryData getInventory(int id){
        InventoryPojo inventoryPojo = inventoryDao.select(id);
        ProductData productData = productService.getProduct(inventoryPojo.getProductId());
        InventoryData inventoryData = new InventoryData();
        inventoryData.setBarcode(productData.getBarcode());
        inventoryData.setProductName(productData.getName());
        inventoryData.setProductId(productData.getId());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }
    @Transactional
    public void updateInventory(InventoryPojo inventoryPojo){
        InventoryPojo oldInventoryPojo = inventoryDao.select(inventoryPojo.getProductId());
        oldInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

    @Transactional
    public void addInventory(InventoryPojo inventoryPojo) throws ApiException{
        InventoryPojo exists = inventoryDao.select(inventoryPojo.getProductId());
        if(exists != null){
            throw new ApiException("Inventory exists for given barcode.");
        }
        inventoryDao.insert(inventoryPojo);
    }
}
