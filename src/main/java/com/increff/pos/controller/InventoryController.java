package com.increff.pos.controller;

import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    ProductService productService;

    private CommonUtils commonUtils;


    @ApiOperation(value = "Get all Inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getInventoryList() {
        return inventoryService.getInventoryList();
    }

    @ApiOperation(value = "Get all Inventories")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData getInventoryOfProduct(@PathVariable int id) {
        return inventoryService.getInventory(id);
    }

    @ApiOperation(value = "Create Inventory of Product")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public void createInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        commonUtils.normalize(inventoryForm);
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        inventoryForm.setProductId(productPojo.getId());
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.addInventory(inventoryPojo);
    }

    @ApiOperation(value = "Update Inventory of Product")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.PUT)
    public void updateInventory(@RequestBody InventoryForm inventoryForm) {
        commonUtils.normalize(inventoryForm);
        InventoryPojo inventoryPojo = convert(inventoryForm);
        inventoryService.updateInventory(inventoryPojo);
    }

    @ApiOperation(value = "Upload Inventory")
    @RequestMapping(path = "/upload", method = RequestMethod.GET)
    public List<UploadErrorMessage> upload(@RequestBody List<InventoryForm> inventoryFormList) {
        return null ;
    }

    private static InventoryPojo convert(InventoryForm inventoryForm){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(inventoryForm.getProductId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }

}
