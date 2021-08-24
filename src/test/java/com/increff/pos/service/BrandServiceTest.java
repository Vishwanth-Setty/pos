package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BrandServiceTest extends AbstractUnitTest {
    @Autowired
    BrandService brandService;

    @Test
    public void testAdd() throws ApiException {

        BrandPojo brandPojo = create("new","cat");
        brandService.addBrand(brandPojo);
        BrandPojo brandPojo1 = brandService.getBrandByNameAndCategory("new","cat");
        assertNotEquals(null, brandPojo1);
    }

    @Test
    public void testDuplicateAdds() {
        try{
            BrandPojo brandPojo = create("new","cat");
            brandService.addBrand(brandPojo);
            BrandPojo brandPojo1 = create("new","cat");
            brandService.addBrand(brandPojo1);
        }
        catch (ApiException apiException){
            assertEquals("Brand and Category already exists",apiException.getMessage());
        }
    }
    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        brandService.addBrand(brandPojo);
        BrandPojo brandPojoNew = brandService.getBrandByNameAndCategory("new","cat");
        assertNotEquals(null,brandPojoNew);
    }

    public void testDuplicateUpdate() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        brandService.addBrand(brandPojo);
        Integer id = brandPojo.getId();
        BrandPojo brandPojoNew = create("new2","cat2");
        brandService.updateBrand(brandPojoNew,id);
        BrandPojo brandPojo1 = brandService.getBrandById(id);
        assertEquals(brandPojo1.getBrand(),brandPojoNew.getBrand());
        assertEquals(brandPojo1.getCategory(),brandPojoNew.getCategory());
    }

    @Test
    public void testGetBrandByNameAndCategory() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        brandService.addBrand(brandPojo);
        BrandPojo exists = brandService.getBrandByNameAndCategory(brandPojo.getBrand(), brandPojo.getCategory());
        assertEquals("new",exists.getBrand());
        assertEquals("cat",exists.getCategory());
    }

    @Test
    public void testGetBrandById() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        brandService.addBrand(brandPojo);
        BrandPojo exists = brandService.getBrandById(brandPojo.getId());
        assertEquals(brandPojo,exists);
    }

    private BrandPojo create(String brand, String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }
}
