package com.increff.pos.controller;

import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
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
    private BrandService service;

    @ApiOperation(value = "Creating new Brand")
    @RequestMapping(path = "",method = RequestMethod.POST)
    public void addBrand(@RequestBody BrandForm brandForm) throws ApiException {
        CommonUtils.normalize(brandForm);                                                        // Passing reference or value
        BrandPojo brandPojo = ConvertUtil.convert(brandForm);
        service.addBrand(brandPojo);
    }

    @ApiOperation(value = "Get all Brand")
    @RequestMapping(path = "",method = RequestMethod.GET)
    public List<BrandData> getAllBrands(){
        List<BrandPojo> listBrandPojo = service.getAllBrands();
        List<BrandData> listBrandData = new ArrayList<>();
        for( BrandPojo brandPojo : listBrandPojo){
            BrandData brandData = ConvertUtil.convert(brandPojo);
            listBrandData.add(brandData);
        }
        return listBrandData;
    }

    @ApiOperation(value = "Get Brand by Id")
    @RequestMapping(path = "/{id}",method = RequestMethod.GET)
    public BrandData getBrand(@PathVariable int id){
        return ConvertUtil.convert(service.getBrandById(id));
    }


    @ApiOperation(value = "Update Brand")
    @RequestMapping(path = "/{id}",method = RequestMethod.PUT)
    public void updateBrand(@PathVariable int id,@RequestBody BrandForm brandForm) throws ApiException{
        CommonUtils.normalize(brandForm);
        BrandPojo brandPojo = ConvertUtil.convert(brandForm);
        brandPojo.setId(id);
        service.updateBrand(brandPojo);
    }

    @ApiOperation(value = "Upload Brands")
    @RequestMapping(path = "/list",method = RequestMethod.POST)
    public void uploadData(@RequestBody List<BrandForm> brandFormList){
        List<BrandPojo> brandPojoList = new ArrayList<BrandPojo>();
        for(BrandForm brandForm : brandFormList){
            CommonUtils.normalize(brandForm);
            brandPojoList.add(ConvertUtil.convert(brandForm));
        }

    }


}
