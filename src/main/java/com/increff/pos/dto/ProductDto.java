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
public class ProductDto extends ValidateUtils<ProductForm> {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    public void add(ProductForm productForm) throws ApiException {
        checkValid(productForm);
        CommonUtils.normalize(productForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convert(productForm,brandPojo.getId());
        productService.addProduct(productPojo);
    }
    public List<ProductData> getAll() throws ApiException {
        List<ProductData> productDataList = new ArrayList<>();
        List<ProductPojo> productPojoList = productService.getProductsList();
        for(ProductPojo productPojo:productPojoList){
            BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
            ProductData productData = ConvertUtil.convert(productPojo,brandPojo.getBrand(),brandPojo.getCategory());
            productDataList.add(productData);
        }
        return productDataList;
    }

    public ProductData getById(int id) throws ApiException{
        ProductPojo productPojo = productService.getProduct(id);
        BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
        return ConvertUtil.convert(productPojo,brandPojo.getBrand(),brandPojo.getCategory());
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        BrandPojo brandPojo = brandService.getBrandById(productPojo.getBrandId());
        return ConvertUtil.convert(productPojo,brandPojo.getBrand(),brandPojo.getCategory());
    }

    public void update(ProductForm productForm,int id) throws ApiException {
        checkValid(productForm);
        CommonUtils.normalize(productForm);
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convert(productForm,brandPojo.getId());
        productService.updateProduct(productPojo,id);
    }

    public void upload(List<ProductForm> productFormList) throws ApiException {
        checkValidList(productFormList);
        String errorMessage = checkData(productFormList);
        if(!errorMessage.equals("")){
            throw new ApiException(errorMessage);
        }
        for(ProductForm productForm : productFormList){
            BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
            ProductPojo productPojo = ConvertUtil.convert(productForm,brandPojo.getId());
            productService.addProduct(productPojo);
        }
    }


    private String checkData(List<ProductForm> productFormList) throws ApiException{
        String errorMessage = "";
        errorMessage = checkDuplicates(productFormList);
        if(!errorMessage.equals("")){
            return "Given TSV have Duplicate field "+errorMessage;
        }
        errorMessage = checkBrandAndCategory(productFormList);
        if(!errorMessage.equals("")){
            return "Given TSV have Invalid Brand and Category Combinations "+errorMessage;
        }
        errorMessage = checkDuplicatesInDatabase(productFormList);
        if(!errorMessage.equals("")){
            return "Given TSV have Duplicate field in Database "+errorMessage;
        }
        return "";
    }

    private  String checkDuplicates(List<ProductForm> productFormList){
        Set<String> hash_Set = new HashSet<String>();
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            if (hash_Set.contains(barcode)) {
                errorMessage.append(Integer.toString(i)).append(" ").append(barcode);
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
            if (exists != null) {
                errorMessage.append(Integer.toString(i)).append(" ").append(barcode);
            }
        }
        return errorMessage.toString();
    }
    private  String checkDuplicatesInDatabase(List<ProductForm> productFormList){
        StringBuilder errorMessage = new StringBuilder();
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            ProductPojo exists = productService.getProductByBarcode(barcode);
            if (exists != null) {
                errorMessage.append(Integer.toString(i)).append(" ").append(barcode);
            }
        }
        return errorMessage.toString();
    }

}
