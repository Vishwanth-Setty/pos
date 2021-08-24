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
import com.increff.pos.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductDto extends ValidateUtils {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    public void add(ProductForm productForm) throws ApiException {
        checkValid(productForm);
        CommonUtils.normalize(productForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
        checkNotNull(brandPojo,"Brand and Category not exists");
        ProductPojo productPojo = ConvertUtil.convert(productForm,brandPojo.getId());
        productService.add(productPojo);
    }
    public List<ProductData> getAll() throws ApiException {
        List<ProductData> productDataList = new ArrayList<>();
        List<ProductPojo> productPojoList = productService.getAll();
        for(ProductPojo productPojo:productPojoList){
            BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
            ProductData productData = ConvertUtil.convert(productPojo,brandPojo.getBrand(),brandPojo.getCategory());
            productDataList.add(productData);
        }
        return productDataList;
    }

    public ProductData getById(int id) throws ApiException{
        ProductPojo productPojo = productService.getById(id);
        BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
        return ConvertUtil.convert(productPojo,brandPojo.getBrand(),brandPojo.getCategory());
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
        return ConvertUtil.convert(productPojo,brandPojo.getBrand(),brandPojo.getCategory());
    }

    public void update(ProductForm productForm,int id) throws ApiException {
        checkValid(productForm);
        CommonUtils.normalize(productForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
        checkNotNull(brandPojo,"Invalid Brand and Category");
        ProductPojo productPojo = ConvertUtil.convert(productForm,brandPojo.getId());
        productService.update(productPojo,id);
    }

    public void upload(List<ProductForm> productFormList) throws ApiException {
        checkValidList(productFormList);
        String errorMessage = checkData(productFormList);

        if(!errorMessage.equals("")){
            throw new ApiException(errorMessage);
        }

        for(ProductForm productForm : productFormList){
            BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
            System.out.println(brandPojo.getId());
            ProductPojo productPojo = ConvertUtil.convert(productForm,brandPojo.getId());
            productService.add(productPojo);
        }
    }


    private String checkData(List<ProductForm> productFormList) throws ApiException{
        String errorMessage = "";
        errorMessage = checkDuplicatesRecords(productFormList);
        if(!errorMessage.equals("")){
            return "Found duplicate barcodes "+errorMessage;
        }
        errorMessage = checkBrandAndCategory(productFormList);
        if(!errorMessage.equals("")){
            return "Invalid Brand and Category pairs  "+errorMessage;
        }
        errorMessage = productService.checkDuplicates(productFormList);
        if(!errorMessage.equals("")){
            return "Barcode already exists  "+errorMessage;
        }
        return "";
    }

    private  String checkDuplicatesRecords(List<ProductForm> productFormList){
        Set<String> hash_Set = new HashSet<String>();
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            if (hash_Set.contains(barcode)) {
                errorMessage.append(" (").append(barcode).append(") ");
            }
            hash_Set.add(barcode);
        }
        return errorMessage.toString();
    }
    private  String checkBrandAndCategory(List<ProductForm> productFormList) {
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            BrandPojo exists = brandService.getBrandByNameAndCategory(brand,category);
            if (exists == null) {
                errorMessage.append(" ").append(barcode).append(") ");
            }
        }
        return errorMessage.toString();
    }

}
