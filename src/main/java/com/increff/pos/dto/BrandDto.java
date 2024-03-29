package com.increff.pos.dto;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BrandDto extends AbstractApi {

    @Autowired
    BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException {
        checkValid(brandForm);
        CommonUtils.normalize(brandForm);
        BrandPojo brandPojo = ConvertUtil.convert(brandForm);
        brandService.addBrand(brandPojo);
    }

    public List<BrandData> getAll() {

        List<BrandPojo> listBrandPojo = brandService.getAllBrands();
        List<BrandData> listBrandData = new ArrayList<>();

        for (BrandPojo brandPojo : listBrandPojo) {
            BrandData brandData = ConvertUtil.convert(brandPojo);
            listBrandData.add(brandData);
        }

        return listBrandData;
    }

    public BrandData getById(int id) {
        return ConvertUtil.convert(brandService.getBrandById(id));
    }

    public void update(int id, BrandForm brandForm) throws ApiException {
        checkValid(brandForm);
        CommonUtils.normalize(brandForm);
        BrandPojo brandPojo = ConvertUtil.convert(brandForm);

        brandService.updateBrand(brandPojo, id);
    }

    public void uploadList(List<BrandForm> brandFormList) throws ApiException {
        checkValidList(brandFormList);
        String errorMessage = validate(brandFormList);

        if (!errorMessage.equals("")) {
            throw new ApiException(errorMessage);
        }

        for (BrandForm brandForm : brandFormList) {
            brandService.addBrand(ConvertUtil.convert(brandForm));
        }
    }

    private String validate(List<BrandForm> brandFormList) {
        String errorMessage = "";

        errorMessage = checkDuplicateRecords(brandFormList);
        if (!errorMessage.equals("")) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
            return "Duplicate records exists for brand-category: [ " + errorMessage + " ]" ;
        }

        errorMessage = checkExists(brandFormList);
        if (!errorMessage.equals("")) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
            return "Brand-Category already exists [" + errorMessage +" ]";
        }

        return errorMessage;
    }

    private static String checkDuplicateRecords(List<BrandForm> brandFormList) {
        StringBuilder errors = new StringBuilder();
        Set<String> hash_Set = new HashSet<String>();

        for (BrandForm brandForm : brandFormList) {
            String brand = brandForm.getBrand();
            String category = brandForm.getCategory();
            String key = brand + '#' + category;

            if (hash_Set.contains(key)) {
                errors.append(brand).append(":").append(category).append(", ");
            }

            hash_Set.add(key);
        }

        return errors.toString();
    }

    private String checkExists(List<BrandForm> brandFormList) {
        StringBuilder errors = new StringBuilder();
        Set<String> brandAndCategory = new HashSet<>();
        List<BrandPojo> brandPojoList = brandService.getAllBrands();
        brandPojoList.forEach(value->{
            brandAndCategory.add(value.getBrand()+'#'+value.getCategory());
        });
        for (BrandForm brandForm : brandFormList) {
            String brand = brandForm.getBrand();
            String category = brandForm.getCategory();
            String key = brand+'#'+category;
            if (brandAndCategory.contains(key)) {
                errors.append(brand).append(":").append(category).append(", ");
            }
        }
        return errors.toString();
    }
}
