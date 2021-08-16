package com.increff.pos.dto;

import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryDto {
    @Autowired
    private InventoryService inventoryApi;
    @Autowired
    private ProductService productApi;

    public void addInventory(InventoryForm inventoryForm) throws ApiException {
        checkValid(inventoryForm);
        CommonUtils.normalize(inventoryForm);
        ProductPojo product = productApi.getCheckByBarcode(inventoryForm.getBarcode());

        inventoryApi.addInventory(ConvertUtil.convert(product.getId(), inventoryForm));
    }

}
