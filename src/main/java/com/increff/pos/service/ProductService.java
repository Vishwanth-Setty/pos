package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)

public class ProductService extends AbstractApi {
    @Autowired
    private ProductDao productDao;

    public ProductPojo add(ProductPojo productPojo) throws ApiException {
        ProductPojo exists = productDao.selectByMethod("barcode",productPojo.getBarcode());
        checkNull(exists,"Barcode already exists");
        return productDao.persist(productPojo);
    }

    public ProductPojo getById(int id) {
        return productDao.select(id);
    }

    public ProductPojo getByBarcode(String barcode) {
        return productDao.selectByMethod("barcode",barcode);
    }

    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    public List<ProductPojo> getByBrandId(int brandId){
        return productDao.selectMultipleByMethod("brandId",brandId);
    }

    public void update(ProductPojo newProductPojo, int id) throws ApiException {
        ProductPojo oldProductPojo = productDao.select(id);
        checkNotNull(oldProductPojo,"Product Id doesn't exists");
        oldProductPojo.setBarcode(newProductPojo.getBarcode());
        oldProductPojo.setBrandId(newProductPojo.getBrandId());
        oldProductPojo.setName(newProductPojo.getName());
        oldProductPojo.setMrp(newProductPojo.getMrp());
    }

}
