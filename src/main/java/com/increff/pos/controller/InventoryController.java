package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class InventoryController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Get all Inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getInventoryList() {
        List<InventoryPojo> inventoryPojoList = inventoryService.getInventoryList();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo : inventoryPojoList){
            inventoryDataList.add(ConvertUtil.convert(inventoryPojo));
        }
        return inventoryDataList;
    }

    @ApiOperation(value = "Get Inventory by Id")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData getInventoryOfProduct(@PathVariable int id) {
        return ConvertUtil.convert(inventoryService.getInventory(id));
    }

    @ApiOperation(value = "Create Inventory of Product")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public void createInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        inventoryDto.addInventory(inventoryForm);
    }

    @ApiOperation(value = "Update Inventory of Product")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.PUT)
    public void updateInventory(@RequestBody InventoryForm inventoryForm) {
        CommonUtils.normalize(inventoryForm);
        InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm);                     //see this as case
        inventoryService.updateInventory(inventoryPojo);
    }

    @ApiOperation(value = "Upload Inventory")
    @RequestMapping(path = "/upload", method = RequestMethod.GET)
    public void upload(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException {
        inventoryService.upload(inventoryFormList);
    }

}
