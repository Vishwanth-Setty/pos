package com.increff.pos.service;

import com.increff.pos.dao.AbstractDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private BrandService brandService;

    @Transactional(rollbackOn  = ApiException.class)
    public void addProduct(ProductPojo productPojo) throws ApiException {
        ProductPojo exists = productDao.selectByBarcode(productPojo.getBarcode());
        if(exists != null){
            throw new ApiException("Product with that barcode exists");
        }
        productDao.insert(productPojo);
    }

    @Transactional
    public ProductData getProduct(int id) {
        ProductPojo productPojo = productDao.select(id);
        BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setBarcode(productPojo.getBarcode());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMrp());
        productData.setBrandId(productPojo.getBrandId());
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory((brandPojo.getCategory()));
        return productData;
    }

    @Transactional
    public ProductPojo getProductByBarcode(String barcode) {
        ProductPojo productPojo = productDao.selectByBarcode(barcode);
        return productPojo;
    }

    @Transactional
    public List<ProductData> getProductsList() {
        List<ProductPojo> listProductPojo = productDao.selectAll();
        List<ProductData> listProductData = new ArrayList<ProductData>();
        for( ProductPojo p : listProductPojo){
            ProductData tempProductData = new ProductData();
            BrandPojo brandPojo = brandService.getBrandById(p.getBrandId());
            tempProductData.setId(p.getId());
            tempProductData.setBarcode(p.getBarcode());
            tempProductData.setName(p.getName());
            tempProductData.setMrp(p.getMrp());
            tempProductData.setBrandId(p.getBrandId());
            tempProductData.setBrand(brandPojo.getBrand());
            tempProductData.setCategory((brandPojo.getCategory()));
            listProductData.add(tempProductData);
        }
        return listProductData;
    }

    @Transactional
    public void updateProduct(ProductPojo newProductPojo) {
        ProductPojo oldProductPojo = productDao.select(newProductPojo.getId());
        oldProductPojo.setBarcode(newProductPojo.getBarcode());
        oldProductPojo.setBrandId(newProductPojo.getBrandId());
        oldProductPojo.setName(newProductPojo.getName());
        oldProductPojo.setMrp(newProductPojo.getMrp());
    }
}
