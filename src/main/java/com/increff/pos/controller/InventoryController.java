package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class InventoryController {

    @Autowired
    InventoryDto inventoryDto;


    @ApiOperation(value = "Get all Inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getInventoryList() {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Get Inventory by barcode")
    @RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.GET)
    public InventoryData getInventoryOfProduct(@PathVariable String barcode) {
        return inventoryDto.get(barcode);
    }

    @ApiOperation(value = "Create Inventory of Product")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public void createInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.add(inventoryForm);
    }

    @ApiOperation(value = "Update Inventory of Product")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.PUT)
    public void updateInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.update(inventoryForm);
    }

    @ApiOperation(value = "Upload Inventory")
    @RequestMapping(path = "/upload", method = RequestMethod.GET)
    public void upload(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException {
        inventoryDto.upload(inventoryFormList);
    }

}
