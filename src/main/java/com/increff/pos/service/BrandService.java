package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService extends AbstractApi {

    @Autowired
    private BrandDao dao;

    public BrandPojo addBrand(BrandPojo brandPojo) throws ApiException {
        BrandPojo exists = dao.selectByNameAndCategory(brandPojo.getBrand(), brandPojo.getCategory());
        checkNull(exists, "Brand and Category already exists");
        brandPojo = dao.persist(brandPojo);
        return brandPojo;
    }

    public List<BrandPojo> getAllBrands() {
        return dao.selectAll();
    }

    public BrandPojo getBrandById(int id) {
        return dao.select(id);
    }

    public BrandPojo getBrandByNameAndCategory(String brand, String category) {
        return dao.selectByNameAndCategory(brand, category);
    }

    public void updateBrand(BrandPojo updatedBrandPojo, int id) throws ApiException {
        BrandPojo brandPojo = getBrandById(id);
        checkNotNull(brandPojo, "Invalid brand and category");

        BrandPojo checkBrandPojo = dao.selectByNameAndCategory(updatedBrandPojo.getBrand(), updatedBrandPojo.getCategory());
        checkNull(checkBrandPojo, "Brand and Category already exists");

        brandPojo.setBrand(updatedBrandPojo.getBrand());
        brandPojo.setCategory(updatedBrandPojo.getCategory());
    }
}
