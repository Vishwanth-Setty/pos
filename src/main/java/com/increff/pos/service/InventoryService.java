package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.ConvertUtil;
import com.increff.pos.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InventoryService extends ErrorUtils {

    @Autowired
    InventoryDao inventoryDao;

    @Transactional
    public List<InventoryPojo> getInventoryList(){
        return inventoryDao.selectAll();
    }

    @Transactional
    public InventoryPojo getInventory(int id){
        return inventoryDao.select(id);
    }

    @Transactional
    public void addInventory(InventoryPojo inventoryPojo) throws ApiException{
        InventoryPojo exists = inventoryDao.select(inventoryPojo.getProductId());
        checkNotNull(exists,"Inventory exists for given barcode.");
        inventoryDao.insert(inventoryPojo);
    }

    @Transactional
    public void updateInventory(InventoryPojo inventoryPojo){
        InventoryPojo oldInventoryPojo = inventoryDao.select(inventoryPojo.getProductId());
        oldInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

    @Transactional
    public void  upload(List<InventoryForm> inventoryFormList) throws  ApiException{
        String errorMessage = checkData(inventoryFormList);
        if(!errorMessage.equals("")){
            throw new ApiException(errorMessage);
        }
        for(InventoryForm inventoryForm : inventoryFormList){
            InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm);
            InventoryPojo exists = inventoryDao.select(inventoryPojo.getProductId());
            if(exists == null){
                addInventory(inventoryPojo);
            }
            else{
                updateInventory(inventoryPojo);
            }
        }
    }

    private String checkData(List<InventoryForm> inventoryFormList) throws ApiException{
        String errorMessage = "";
        errorMessage = checkEmpty(inventoryFormList);
        if(!errorMessage.equals("")){
            return "Given rows have empty field "+errorMessage;
        }
        errorMessage = checkDuplicates(inventoryFormList);
        if(!errorMessage.equals("")){
            return "Given TSV have Duplicate field "+errorMessage;
        }
        return "";
    }
    private String checkEmpty(List<InventoryForm> inventoryFormList){
        String errorMessage = "";
        int i = 1;
        for(InventoryForm inventoryForm: inventoryFormList){
            ++i;
            String barcode = inventoryForm.getBarcode();
            int quantity = inventoryForm.getQuantity();
            if (barcode.equals("") || quantity == 0) {
                errorMessage += Integer.toString(i) ;
            }
        }
        return errorMessage;

    }

    private  String checkDuplicates(List<InventoryForm> inventoryFormList){
        Set<String> hash_Set = new HashSet<String>();
        String errorMessage = "";
        int i = 1;
        for(InventoryForm inventoryForm:inventoryFormList){
            ++i;
            String barcode = inventoryForm.getBarcode();
            if (hash_Set.contains(barcode)) {
                errorMessage += Integer.toString(i) + " " + barcode ;
            }
            hash_Set.add(barcode);
        }
        return errorMessage;
    }

//    private  String checkQuantity(List<InventoryForm> inventoryFormList){
//        String errorMessage = "";
//        int i = 1;
//        for(InventoryForm inventoryForm:inventoryFormList){
//            ++i;
//            int quantity = inventoryForm.getQuantity();
//            if (hash_Set.contains(barcode)) {
//                errorMessage += Integer.toString(i) + " " + barcode ;
//            }
//            hash_Set.add(barcode);
//        }
//        return errorMessage;
//    }

}
