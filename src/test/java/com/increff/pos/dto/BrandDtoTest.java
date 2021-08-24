package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    BrandService brandService;

    @Autowired
    BrandDto brandDto;

    @Test
    public void testAdd() throws ApiException {
        BrandForm brandForm = create("new","cat");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory("new","cat");
        assertEquals(brandPojo.getBrand(),brandForm.getBrand());
        assertEquals(brandPojo.getCategory(),brandForm.getCategory());
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandForm brandForm = create("new","cat");
        brandDto.add(brandForm);
        List<BrandData> brandDataList = brandDto.getAll();
        assertEquals(brandDataList.size(),1);
    }

    @Test
    public void testGetById() throws ApiException{
        BrandForm brandForm = create("new","cat");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory("new","cat");
        BrandData brandData = brandDto.getById(brandPojo.getId());
        assertNotEquals(null,brandData);
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandForm brandForm = create("new","cat");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory("new","cat");
        brandForm = create("new1","cat1");
        brandDto.update(brandPojo.getId(),brandForm);
        brandPojo = brandService.getBrandByNameAndCategory("new1","cat1");
        assertNotEquals(null,brandPojo);
    }
    @Test
    public void testUpload() throws ApiException {
        List<BrandForm> brandFormList = new ArrayList<>();
        brandFormList.add(create("new","cat"));
        brandFormList.add(create("new1","cat1"));
        brandDto.uploadList(brandFormList);
        List<BrandData> brandDataList = brandDto.getAll();
        assertEquals(brandDataList.size(),2);
    }


    private BrandForm create(String brand,String category){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }
}
