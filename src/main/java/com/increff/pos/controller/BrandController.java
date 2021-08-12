package com.increff.pos.controller;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
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
    private BrandService service;

    @ApiOperation(value = "Creating new Brand")
    @RequestMapping(path = "",method = RequestMethod.POST)
    public void addBrand(@RequestBody BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = convert(brandForm);
        service.addBrand(brandPojo);
    }

    @ApiOperation(value = "Get all Brand")
    @RequestMapping(path = "",method = RequestMethod.GET)
    public List<BrandData> getAllBrands(){
        List<BrandPojo> listBrandPojo = service.getAllBrands();
        List<BrandData> listBrandData = new ArrayList<BrandData>();
        for( BrandPojo p : listBrandPojo){
            listBrandData.add(convert(p));
        }
        return listBrandData;
    }

    @ApiOperation(value = "Get Brand by Id")
    @RequestMapping(path = "/{id}",method = RequestMethod.GET)
    public BrandData getBrand(@PathVariable int id){
        return convert(service.getBrandById(id));
    }


    @ApiOperation(value = "Update Brand")
    @RequestMapping(path = "/{id}",method = RequestMethod.PUT)
    public void updateBrand(@PathVariable int id,@RequestBody BrandForm brandForm) throws ApiException{
        BrandPojo brandPojo = convert(brandForm);
        brandPojo.setId(id);
        service.updateBrand(brandPojo);
    }

    private static BrandData convert(BrandPojo p){
        BrandData d = new BrandData();
        d.setId(p.getId());
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        return d;
    }

    private static BrandPojo convert(BrandForm form){
        BrandPojo p = new BrandPojo();
        p.setBrand(form.getBrand());
        p.setCategory(form.getCategory());
        return p;
    }
}
