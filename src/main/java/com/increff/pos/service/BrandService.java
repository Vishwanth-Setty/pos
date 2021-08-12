package com.increff.pos.service;

import com.google.protobuf.Api;
import com.increff.pos.dao.AbstractDao;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandDao dao;

    @Transactional
    public void addBrand(BrandPojo brandPojo) throws ApiException {
        BrandPojo exists = dao.selectByNameAndCategory(brandPojo.getBrand(),brandPojo.getCategory());
        if(exists != null){
            throw new ApiException("Brand name with "+ brandPojo.getBrand() + " exists in " + brandPojo.getCategory());
        }
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
        if(brandPojo == null){
            throw new ApiException("Brand doest exists with that combination");
        }
        return brandPojo;
    }

    @Transactional
    public void deleteBrand(int id) {
        dao.delete(id);
    }

    @Transactional
    public void updateBrand(BrandPojo p) throws ApiException {
        BrandPojo checkBrandPojo = dao.selectByNameAndCategory(p.getBrand(),p.getCategory());
        if(checkBrandPojo != null){
            throw new ApiException("Brand name with "+ p.getBrand() + " exists in " + p.getCategory());
        }
        BrandPojo brandPojo = dao.select(p.getId());
        brandPojo.setBrand(p.getBrand());
        brandPojo.setCategory(p.getCategory());
    }

}
