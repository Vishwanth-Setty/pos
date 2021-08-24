package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BrandService extends AbstractApi {

    @Autowired
    private BrandDao dao;

    @Transactional
    public BrandPojo addBrand(BrandPojo brandPojo) throws ApiException {
        BrandPojo exists = dao.selectByNameAndCategory(brandPojo.getBrand(),brandPojo.getCategory());
        checkNull(exists,"Brand and Category already exists");
        brandPojo = dao.persist(brandPojo);
        return brandPojo;
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
    public BrandPojo getBrandByNameAndCategory (String brand,String category) {
        return dao.selectByNameAndCategory(brand,category);
    }

    @Transactional
    public void updateBrand(BrandPojo p,int id) throws ApiException {
        BrandPojo brandPojo = getBrandById(id);
        checkNotNull(brandPojo,"Invalid Brand Id");

        BrandPojo checkBrandPojo = dao.selectByNameAndCategory(p.getBrand(),p.getCategory());
        checkNull(checkBrandPojo,"Brand and Category already exists");

        brandPojo.setBrand(p.getBrand());
        brandPojo.setCategory(p.getCategory());
    }
}
