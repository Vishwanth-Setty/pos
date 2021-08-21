package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.utils.CommonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.increff.pos.utils.CommonUtils.normalize;
import static org.junit.Assert.assertEquals;

public class BrandServiceTest extends  AbstractUnitTest {
    @Autowired
    BrandService brandService;
    @Test
    public void testAdd() {
        try{
            BrandPojo brandPojo = createBrand("new","cat");
            brandService.addBrand(brandPojo);
            BrandPojo brandPojo1 = createBrand("new","cat");
            brandService.addBrand(brandPojo1);
        }
        catch (ApiException apiException){
            assertEquals("Brand and Category Exists",apiException.getMessage());
        }

    }
    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = createBrand("new","cat");
        brandService.addBrand(brandPojo);
        Integer id = brandPojo.getId();
        BrandPojo brandPojoNew = createBrand("new2","cat2");
        brandService.updateBrand(brandPojoNew,id);
        BrandPojo brandPojo1 = brandService.getBrandById(id);
        assertEquals(brandPojo1.getBrand(),brandPojoNew.getBrand());
        assertEquals(brandPojo1.getCategory(),brandPojoNew.getCategory());
    }

    @Test
    public void testGetBrandByNameAndCategory() throws ApiException {
        BrandPojo brandPojo = createBrand("new","cat");
        brandService.addBrand(brandPojo);
        BrandPojo exists = brandService.getBrandByNameAndCategory(brandPojo.getBrand(), brandPojo.getCategory());
        assertEquals("new",exists.getBrand());
        assertEquals("cat",exists.getCategory());
    }

    @Test
    public void testGetBrandById() throws ApiException {
        BrandPojo brandPojo = createBrand("new","cat");
        brandService.addBrand(brandPojo);
        BrandPojo exists = brandService.getBrandById(brandPojo.getId());
        assertEquals(brandPojo,exists);
    }

    @Test
    public void testNormalize() {
        BrandPojo brandPojo = createBrand(" new ","cat");
        normalize(brandPojo);
        assertEquals("new", brandPojo.getBrand());
    }


}
