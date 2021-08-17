package com.increff.pos.dto;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import com.increff.pos.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandDto extends ValidateUtils<BrandForm> {
    @Autowired
    BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        checkValid(brandForm);
        CommonUtils.normalize(brandForm);                                                        // Passing reference or value
        BrandPojo brandPojo = ConvertUtil.convert(brandForm);
        brandService.addBrand(brandPojo);
    }
    public List<BrandData> getAll(){

        List<BrandPojo> listBrandPojo = brandService.getAllBrands();
        List<BrandData> listBrandData = new ArrayList<>();
        for( BrandPojo brandPojo : listBrandPojo){
            BrandData brandData = ConvertUtil.convert(brandPojo);
            listBrandData.add(brandData);
        }
        return listBrandData;
    }

    public BrandData getById(int id){
        return ConvertUtil.convert(brandService.getBrandById(id));
    }

    public void update(int id,BrandForm brandForm) throws ApiException{
        checkValid(brandForm);
        CommonUtils.normalize(brandForm);
        BrandPojo brandPojo = ConvertUtil.convert(brandForm);
        brandPojo.setId(id);
        brandService.updateBrand(brandPojo);
    }

    public void uploadList(List<BrandForm> brandFormList){

    }
}
