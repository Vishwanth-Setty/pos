package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
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
@RequestMapping(value = "/api/brand")
@RestController
public class BrandController {

    @Autowired
    private BrandDto brandDto;


    @ApiOperation(value = "Creating new Brand")
    @RequestMapping(path = "",method = RequestMethod.POST)
    public void addBrand(@RequestBody BrandForm brandForm) throws ApiException {
        brandDto.add(brandForm);
    }

    @ApiOperation(value = "Get all Brand")
    @RequestMapping(path = "",method = RequestMethod.GET)
    public List<BrandData> getAllBrands() {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Get Brand by Id")
    @RequestMapping(path = "/{id}",method = RequestMethod.GET)
    public BrandData getBrand(@PathVariable int id){
        return brandDto.getById(id);
    }


    @ApiOperation(value = "Update Brand")
    @RequestMapping(path = "/{id}",method = RequestMethod.PUT)
    public void updateBrand(@PathVariable int id,@RequestBody BrandForm brandForm) throws ApiException{
        brandDto.update(id,brandForm);
    }

    @ApiOperation(value = "Upload Brands")
    @RequestMapping(path = "/list",method = RequestMethod.POST)
    public void uploadData(@RequestBody List<BrandForm> brandFormList) throws ApiException {
        brandDto.uploadList(brandFormList);
    }


}
