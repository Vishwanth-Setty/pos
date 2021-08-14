package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Transactional
    public List<UploadErrorMessage> upload(List<InventoryForm> inventoryFormList) throws  ApiException{
        List<UploadErrorMessage> uploadErrorMessageList = checkData(inventoryFormList);
        if(uploadErrorMessageList.size()!=0){
            return uploadErrorMessageList;
        }
        for(InventoryForm inventoryForm : inventoryFormList){
            String barcode = inventoryForm.getBarcode();
            ProductPojo productPojo = productService.getProductByBarcode(barcode);
            InventoryPojo inventoryPojo1 = inventoryDao.select(inventoryForm.getProductId());
            if(inventoryPojo1 == null){
                InventoryPojo inventoryPojo = new InventoryPojo();
                inventoryPojo.setProductId(productPojo.getId());
                inventoryPojo.setQuantity(inventoryForm.getQuantity());
                addInventory(inventoryPojo);
            }
            else{
                InventoryPojo inventoryPojo = new InventoryPojo();
                inventoryPojo.setProductId(productPojo.getId());
                inventoryPojo.setQuantity(inventoryForm.getQuantity());
                updateInventory(inventoryPojo);
            }
        }
        return null;
    }

    @Transactional
    private List<UploadErrorMessage> checkData(List<InventoryForm> inventoryFormList){
        List<UploadErrorMessage> uploadErrorMessageList = new ArrayList<UploadErrorMessage>();
        Set<String> hash_Set = new HashSet<String>();
        int row = 0;
        for(InventoryForm inventoryForm : inventoryFormList){
            ++row;
            String barcode = inventoryForm.getBarcode();
            ProductPojo productPojo = productService.getProductByBarcode(barcode);
            if(productPojo == null){
                UploadErrorMessage uploadErrorMessage = new UploadErrorMessage();
                uploadErrorMessage.setRowNumber(row);
                uploadErrorMessage.setError("Invalid barcode");
                uploadErrorMessageList.add(uploadErrorMessage);
                continue;
            }
            if(hash_Set.contains(barcode)){
                UploadErrorMessage uploadErrorMessage = new UploadErrorMessage();
                uploadErrorMessage.setRowNumber(row);
                uploadErrorMessage.setError("Barcode already exists in the TSV");
                uploadErrorMessageList.add(uploadErrorMessage);
                continue;
            }
            hash_Set.add(barcode);
        }
        return uploadErrorMessageList;
    }
}
