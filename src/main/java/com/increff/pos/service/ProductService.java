package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService extends ValidateUtils {
    @Autowired
    private ProductDao productDao;
    @Transactional
    public ProductPojo add(ProductPojo productPojo) throws ApiException {
        ProductPojo exists = productDao.selectByBarcode(productPojo.getBarcode());
        checkNull(exists,"Barcode exists");
        return productDao.insert(productPojo);
    }

    @Transactional
    public ProductPojo getById(int id) {
        return productDao.select(id);
    }

    @Transactional
    public ProductPojo getByBarcode(String barcode) {
        return productDao.selectByBarcode(barcode);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    @Transactional
    public List<ProductPojo> getByBrandId(int brandId){
        return productDao.selectByBrandId(brandId);
    }

    @Transactional
    public void update(ProductPojo newProductPojo, int id) throws ApiException {
        ProductPojo oldProductPojo = productDao.select(id);
        checkNotNull(oldProductPojo,"Invalid Product Id");
        oldProductPojo.setBarcode(newProductPojo.getBarcode());
        oldProductPojo.setBrandId(newProductPojo.getBrandId());
        oldProductPojo.setName(newProductPojo.getName());
        oldProductPojo.setMrp(newProductPojo.getMrp());
    }

    @Transactional
    public String checkDuplicates(List<ProductForm> productFormList){
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            ProductPojo exists = getByBarcode(barcode);
            if (exists != null) {
                errorMessage.append(" ").append(barcode).append(") ");
            }
        }
        return errorMessage.toString();
    }

}
