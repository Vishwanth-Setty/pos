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
@RequestMapping(value="/api/inventory")
@RestController
public class InventoryController {

    @Autowired
    InventoryDto inventoryDto;

    @ApiOperation(value = "Get all Inventories")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<InventoryData> getList() {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Get Inventory by barcode")
    @RequestMapping(path = "/{barcode}", method = RequestMethod.GET)
    public InventoryData getInventoryOfProduct(@PathVariable String barcode) {
        return inventoryDto.get(barcode);
    }

    @ApiOperation(value = "Create Inventory of Product")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void create(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.add(inventoryForm);
    }

    @ApiOperation(value = "Update Inventory of Product")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void update(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.update(inventoryForm);
    }

    @ApiOperation(value = "Upload Inventory")
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public void upload(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException {
        inventoryDto.upload(inventoryFormList);
    }

}
