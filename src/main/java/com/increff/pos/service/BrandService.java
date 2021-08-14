package com.increff.pos.service;

import com.google.protobuf.Api;
import com.increff.pos.dao.AbstractDao;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import com.increff.pos.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BrandService extends ErrorUtils {

    @Autowired
    private BrandDao dao;

    private CommonUtils commonUtils;
    private ConvertUtil convertUtil;

    @Transactional
    public void addBrand(BrandPojo brandPojo) throws ApiException {
        BrandPojo exists = dao.selectByNameAndCategory(brandPojo.getBrand(),brandPojo.getCategory());
        checkNotNull(exists,"Brand and Category Exists");
//        if(exists != null){
//            throw new ApiException("Brand name with "+ brandPojo.getBrand() + " exists in " + brandPojo.getCategory());
//        }
        dao.insert(brandPojo);
    }

    @Transactional
    public List<BrandPojo> getAllBrands() {
        return dao.selectAll();
    }

    @Transactional
    public BrandPojo getBrandById(int id)    {
        return dao.select(id);
    }

    @Transactional
    public BrandPojo getBrandByNameAndCategory (String brand,String category) throws ApiException {
        BrandPojo brandPojo = dao.selectByNameAndCategory(brand,category);
        checkNull(brandPojo,"Brand doest exists with that combination");
//        if(brandPojo == null){
//            throw new ApiException("Brand doest exists with that combination");
//        }
        return brandPojo;
    }

    @Transactional
    public void updateBrand(BrandPojo p) throws ApiException {
        BrandPojo checkBrandPojo = dao.selectByNameAndCategory(p.getBrand(),p.getCategory());
        checkNotNull(checkBrandPojo,"Brand and Category Exists");
//        if(checkBrandPojo != null){
//            throw new ApiException("Brand name with "+ p.getBrand() + " exists in " + p.getCategory());
//        }
        BrandPojo brandPojo = dao.select(p.getId());
        brandPojo.setBrand(p.getBrand());
        brandPojo.setCategory(p.getCategory());
    }
    @Transactional
    public List<UploadErrorMessage> upload(List<BrandPojo> brandPojoList) throws ApiException{
        String errorMessage = checkData(brandPojoList);
        if(errorMessage != ""){
            throw new ApiException(errorMessage);
        }
        for(BrandPojo brandPojo:brandPojoList){
            addBrand(brandPojo);
        }
        return null;
    }

    @Transactional
    private String checkData(List<BrandPojo> brandPojoList){
        Set<String> hash_Set = new HashSet<String>();
        String errorMessage = "";
        errorMessage = checkEmpty(brandPojoList);
        if(!errorMessage.equals("")){
            return "Given rows have empty field "+errorMessage;
        }
        errorMessage = checkDuplicates(brandPojoList);
        if(!errorMessage.equals("")){
            return "Given TSV have Duplicate field "+errorMessage;
        }
        errorMessage = checkDuplicatesInDatabase(brandPojoList);
        if(!errorMessage.equals("")){
            return "Given TSV have Duplicate field in Database "+errorMessage;
        }
        return "";
    }

    private static String checkEmpty(List<BrandPojo> brandPojoList){
        StringBuilder errors = new StringBuilder();
        for(BrandPojo brandPojo:brandPojoList){
            String brand = brandPojo.getBrand();
            String category = brandPojo.getCategory();
            if(brand.equals("") || category.equals("") ) {
                errors.append(" ( ").append(brand).append(" ").append(category).append(" ) ");
            }
        }
        return errors.toString();
    }

    private static String checkDuplicates(List<BrandPojo> brandPojoList){
        StringBuilder errors = new StringBuilder();
        Set<String> hash_Set = new HashSet<String>();
        for(BrandPojo brandPojo:brandPojoList){
            String brand = brandPojo.getBrand();
            String category = brandPojo.getCategory();
            String key = brand+'#'+category;
            if(hash_Set.contains(key)){
                errors.append(" ( ").append(brand).append(" ").append(category).append(" ) ");
            }
            hash_Set.add(key);
        }
        return errors.toString();
    }

    private String checkDuplicatesInDatabase(List<BrandPojo> brandPojoList){
        StringBuilder errors = new StringBuilder();
        for(BrandPojo brandPojo:brandPojoList){
            String brand = brandPojo.getBrand();
            String category = brandPojo.getCategory();
            BrandPojo brandExists = dao.selectByNameAndCategory(brand,category);
            if(brand.equals("") || category.equals("") ) {
                errors.append(" ( ").append(brand).append(" ").append(category).append(" ) ");
            }
        }
        return errors.toString();
    }
}
