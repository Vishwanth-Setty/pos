package com.increff.pos.dto;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductDto extends AbstractApi {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    public void add(ProductForm productForm) throws ApiException {
        checkValid(productForm);
        CommonUtils.normalize(productForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(), productForm.getCategory());
        checkNotNull(brandPojo, "Invalid Brand and Category");
        ProductPojo productPojo = ConvertUtil.convert(productForm, brandPojo.getId());
        productService.add(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductData> productDataList = new ArrayList<>();
        List<ProductPojo> productPojoList = productService.getAll();
        for (ProductPojo productPojo : productPojoList) {
            BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
            ProductData productData = ConvertUtil.convert(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
            productDataList.add(productData);
        }
        return productDataList;
    }

    public ProductData getById(int id) throws ApiException {
        ProductPojo productPojo = productService.getById(id);
        BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
        return ConvertUtil.convert(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
        return ConvertUtil.convert(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
    }

    public void update(ProductForm productForm, int id) throws ApiException {
        checkValid(productForm);
        CommonUtils.normalize(productForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(), productForm.getCategory());
        checkNotNull(brandPojo, "Invalid Brand and Category");
        ProductPojo productPojo = ConvertUtil.convert(productForm, brandPojo.getId());
        productService.update(productPojo, id);
    }

    public void upload(List<ProductForm> productFormList) throws ApiException {
        checkValidList(productFormList);
        String errorMessage = validate(productFormList);

        if (!errorMessage.equals("")) {
            throw new ApiException(errorMessage);
        }

        for (ProductForm productForm : productFormList) {
            BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(), productForm.getCategory());
            ProductPojo productPojo = ConvertUtil.convert(productForm, brandPojo.getId());
            productService.add(productPojo);
        }
    }


    private String validate(List<ProductForm> productFormList) throws ApiException {
        String errorMessage = "";
        errorMessage = checkDuplicatesRecords(productFormList);
        if (!errorMessage.equals("")) {
            return "Duplicate records exists for barcode [ " + errorMessage + " ]";
        }
        errorMessage = checkBrandAndCategory(productFormList);
        if (!errorMessage.equals("")) {
            return "Invalid brand and category combinations for brand-category: [ " + errorMessage + " ]";
        }
        errorMessage = checkExists(productFormList);
        if (!errorMessage.equals("")) {
            return "Barcode already exists [ " + errorMessage +" ]";
        }
        return "";
    }

    private String checkDuplicatesRecords(List<ProductForm> productFormList) {
        Set<String> hash_Set = new HashSet<String>();
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for (ProductForm productForm : productFormList) {
            ++i;
            String barcode = productForm.getBarcode();
            if (hash_Set.contains(barcode)) {
                errorMessage.append("").append(barcode).append(", ");
            }
            hash_Set.add(barcode);
        }
        return errorMessage.toString();
    }

    private String checkBrandAndCategory(List<ProductForm> productFormList) {
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for (ProductForm productForm : productFormList) {
            ++i;
            Set<String> brandAndCategory = new HashSet<>();
            List<BrandPojo> brandPojoList = brandService.getAllBrands();
            brandPojoList.forEach(value->{
                brandAndCategory.add(value.getBrand()+'#'+value.getCategory());
            });
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            String key = brand+'#'+category;

            String barcode = productForm.getBarcode();

            if (!brandAndCategory.contains(key)) {
                errorMessage.append(" ").append(barcode).append(", ");
            }
        }
        return errorMessage.toString();
    }

    @Transactional
    public String checkExists(List<ProductForm> productFormList){
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        List<ProductPojo> productPojoList = productService.getAll();
        HashSet<String> barcodes = new HashSet<>();
        productPojoList.forEach(productPojo -> {
            barcodes.add(productPojo.getBarcode());
        });
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            if (barcodes.contains(barcode)) {
                errorMessage.append("").append(barcode).append(", ");
            }
        }
        return errorMessage.toString();
    }

}
