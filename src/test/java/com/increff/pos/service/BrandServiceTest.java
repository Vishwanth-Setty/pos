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
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(" abc ");
        brandPojo.setCategory("asdf");
        brandService.addBrand(brandPojo);
    }
    @Test
    public void testNormalize() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(" abc ");
        brandPojo.setCategory("asdf");
        normalize(brandPojo);
        assertEquals("abc", brandPojo.getBrand());
    }

}
