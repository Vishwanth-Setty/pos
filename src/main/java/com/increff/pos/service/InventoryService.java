package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

//TODO remove comments
@Service
public class InventoryService extends ValidateUtils {

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
        checkNull(exists,"Inventory already exists");
        return inventoryDao.insert(inventoryPojo);
    }

    @Transactional
    public void update(InventoryPojo inventoryPojo){
        InventoryPojo oldInventoryPojo = inventoryDao.select(inventoryPojo.getProductId());
        oldInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

//    @Transactional
//    public void  upload(List<InventoryForm> inventoryFormList) throws  ApiException{
//        String errorMessage = checkData(inventoryFormList);
//        if(!errorMessage.equals("")){
//            throw new ApiException(errorMessage);
//        }
//        for(InventoryForm inventoryForm : inventoryFormList){
//            InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm);
//            InventoryPojo exists = inventoryDao.select(inventoryPojo.getProductId());
//            if(exists == null){
//                addInventory(inventoryPojo);
//            }
//            else{
//                updateInventory(inventoryPojo);
//            }
//        }
//    }

//    private String checkData(List<InventoryForm> inventoryFormList) throws ApiException{
//        String errorMessage = "";
//        errorMessage = checkEmpty(inventoryFormList);
//        if(!errorMessage.equals("")){
//            return "Given rows have empty field "+errorMessage;
//        }
//        errorMessage = checkDuplicates(inventoryFormList);
//        if(!errorMessage.equals("")){
//            return "Given TSV have Duplicate field "+errorMessage;
//        }
//        return "";
//    }
//    private String checkEmpty(List<InventoryForm> inventoryFormList){
//        String errorMessage = "";
//        int i = 1;
//        for(InventoryForm inventoryForm: inventoryFormList){
//            ++i;
//            String barcode = inventoryForm.getBarcode();
//            int quantity = inventoryForm.getQuantity();
//            if (barcode.equals("") || quantity == 0) {
//                errorMessage += Integer.toString(i) ;
//            }
//        }
//        return errorMessage;
//
//    }
//
//    private  String checkDuplicates(List<InventoryForm> inventoryFormList){
//        Set<String> hash_Set = new HashSet<String>();
//        String errorMessage = "";
//        int i = 1;
//        for(InventoryForm inventoryForm:inventoryFormList){
//            ++i;
//            String barcode = inventoryForm.getBarcode();
//            if (hash_Set.contains(barcode)) {
//                errorMessage += Integer.toString(i) + " " + barcode ;
//            }
//            hash_Set.add(barcode);
//        }
//        return errorMessage;
//    }

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
