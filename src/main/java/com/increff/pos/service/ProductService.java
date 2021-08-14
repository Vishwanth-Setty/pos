package com.increff.pos.service;

import com.google.protobuf.Api;
import com.increff.pos.dao.AbstractDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private BrandService brandService;

    private CommonUtils commonUtils;

    @Transactional(rollbackOn = ApiException.class)
    public void addProduct(ProductPojo productPojo) throws ApiException {
        ProductPojo exists = productDao.selectByBarcode(productPojo.getBarcode());
        if (exists != null) {
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
        return productDao.selectByBarcode(barcode);
    }

    @Transactional
    public List<ProductData> getProductsList() {
        List<ProductPojo> listProductPojo = productDao.selectAll();
        List<ProductData> listProductData = new ArrayList<ProductData>();
        for (ProductPojo p : listProductPojo) {
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

    @Transactional
    public List<UploadErrorMessage> upload(List<ProductForm> productFormList) throws ApiException {
        List<UploadErrorMessage> uploadErrorMessageList = checkData(productFormList);
        if (uploadErrorMessageList.size() != 0) {
            return uploadErrorMessageList;
        }
        for (ProductForm productForm : productFormList) {
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            BrandPojo brandPojo = brandService.getBrandByNameAndCategory(brand, category);
            productForm.setBrandId(brandPojo.getId());
            ProductPojo productPojo = convert(productForm);
            addProduct(productPojo);
        }
        return null;
    }

    @Transactional
    private List<UploadErrorMessage> checkData(List<ProductForm> productFormList) throws ApiException {
        List<UploadErrorMessage> uploadErrorMessageList = new ArrayList<UploadErrorMessage>();
        Set<String> hash_Set = new HashSet<String>();
        int row = 0;
        for (ProductForm productForm : productFormList) {
            row++;
            ProductPojo productPojo = getProductByBarcode(productForm.getBarcode());
            String barcode = productForm.getBarcode();
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            double mrp = productForm.getMrp();
            if (barcode.equals("") || brand.equals("") || category.equals("") || mrp == 0) {
                uploadErrorMessageList.add(commonUtils.setError(row, "Fields cant be empty"));
                continue;
            }

            BrandPojo brandPojo = brandService.getBrandByNameAndCategory(brand, category);
            if (hash_Set.contains(barcode)) {
                uploadErrorMessageList.add(commonUtils.setError(row, "Barcode already exists in the TSV"));
                continue;
            }
            if (productPojo != null) {
                uploadErrorMessageList.add(commonUtils.setError(row, "Barcode already exists in the Database"));
                continue;
            }

            if (brandPojo != null) {
                uploadErrorMessageList.add(commonUtils.setError(row, "Brand and Category doesn't exists"));
                continue;
            }
            if (mrp < 0) {
                uploadErrorMessageList.add(commonUtils.setError(row, "MRP should be Positive Value"));
                continue;
            }
            hash_Set.add(barcode);
        }

        return uploadErrorMessageList;
    }

    private static ProductPojo convert(ProductForm form) {
        ProductPojo p = new ProductPojo();
        p.setBarcode(form.getBarcode());
        p.setBrandId(form.getBrandId());
        p.setName(form.getName());
        p.setMrp(form.getMrp());
        return p;
    }
}
