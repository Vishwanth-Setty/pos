package com.increff.pos.dto;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import com.increff.pos.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InventoryDto extends ValidateUtils<InventoryForm> {
    @Autowired
    InventoryService inventoryService;

    @Autowired
    ProductService productService;

    public void add(InventoryForm inventoryForm) throws ApiException {
        checkValid(inventoryForm);
        ProductPojo productPojo = productService.getByBarcode(inventoryForm.getBarcode());
        CommonUtils.normalize(inventoryForm);
        InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm,productPojo.getId());
        inventoryService.add(inventoryPojo);
    }
    public List<InventoryData> getAll(){
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo : inventoryPojoList){
            ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
            inventoryDataList.add(ConvertUtil.convert(inventoryPojo,productPojo.getBarcode(),productPojo.getName()));
        }
        return inventoryDataList;
    }
    public InventoryData get(String barcode){
        ProductPojo productPojo = productService.getByBarcode(barcode);
        return ConvertUtil.convert(inventoryService.getById(productPojo.getId()),
                productPojo.getBarcode(),productPojo.getName());
    }

    public void update(InventoryForm inventoryForm) throws ApiException {
        checkValid(inventoryForm);
        ProductPojo productPojo = productService.getByBarcode(inventoryForm.getBarcode());
        CommonUtils.normalize(inventoryForm);
        InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm,productPojo.getId());                     //see this as case
        inventoryService.update(inventoryPojo);
    }

    public void upload(List<InventoryForm> inventoryFormList) throws ApiException {
        checkValidList(inventoryFormList);
        String errorMessage = checkData(inventoryFormList);
        if(!errorMessage.equals("")){
            throw new ApiException(errorMessage);
        }
        for(InventoryForm inventoryForm : inventoryFormList){
            ProductPojo productPojo = productService.getByBarcode(inventoryForm.getBarcode());
            InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm,productPojo.getId());
            InventoryPojo exists = inventoryService.getById(inventoryPojo.getProductId());
            if(exists == null){
                inventoryService.add(inventoryPojo);
            }
            else{
                inventoryService.update(inventoryPojo);
            }
        }
    }
    private String checkData(List<InventoryForm> inventoryFormList) throws ApiException{
        String errorMessage = "";
        errorMessage = checkDuplicates(inventoryFormList);
        if(!errorMessage.equals("")){
            return "Given TSV have Duplicate field "+errorMessage;
        }
        errorMessage = checkBarcode(inventoryFormList);
        if(!errorMessage.equals("")){
            return "Given TSV have Invalid Barcode field in "+errorMessage;
        }
        return "";
    }

    private  String checkDuplicates(List<InventoryForm> inventoryFormList){
        Set<String> hash_Set = new HashSet<String>();
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for(InventoryForm inventoryForm:inventoryFormList){
            ++i;
            String barcode = inventoryForm.getBarcode();
            if (hash_Set.contains(barcode)) {
                errorMessage.append(Integer.toString(i)).append(" ").append(barcode);
            }
            hash_Set.add(barcode);
        }
        return errorMessage.toString();
    }

    private  String checkBarcode(List<InventoryForm> inventoryFormList){
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for(InventoryForm inventoryForm:inventoryFormList){
            ++i;
            String barcode = inventoryForm.getBarcode();
            ProductPojo productPojo = productService.getByBarcode(barcode);
            if (productPojo == null) {
                errorMessage.append(Integer.toString(i)).append(" ").append(barcode);
            }
        }
        return errorMessage.toString();
    }
}
