package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.CommonUtils;
import com.increff.pos.utils.ConvertUtil;
import com.increff.pos.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService extends ErrorUtils {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private BrandService brandService;

    private CommonUtils commonUtils;

    @Transactional
    public void addProduct(ProductPojo productPojo) throws ApiException {
        ProductPojo exists = productDao.selectByBarcode(productPojo.getBarcode());
        checkNotNull(exists,"Product with that barcode exists");
        productDao.insert(productPojo);
    }

    @Transactional
    public ProductPojo getProduct(int id) {
        return productDao.select(id);
    }

    @Transactional
    public ProductPojo getByBarcode(String barcode) {
        return productDao.selectByBarcode(barcode);
    }

    @Transactional
    public ProductPojo getCheckByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productDao.selectByBarcode(barcode);
        checkNotNull(productPojo, "Product with barcode "+ barcode +" doesn't exists");

        return productPojo;
    }

    @Transactional
    public List<ProductPojo> getProductsList() {
        return productDao.selectAll();
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
    public void upload(List<ProductForm> productFormList) throws ApiException {
        String errorMessage = checkData(productFormList);
        if(!errorMessage.equals("")){
            throw new ApiException(errorMessage);
        }
        for(ProductForm productForm : productFormList){
            ProductPojo productPojo = ConvertUtil.convert(productForm);
            addProduct(productPojo);
        }
    }

    private String checkData(List<ProductForm> productFormList) throws ApiException{
        String errorMessage = "";
        errorMessage = checkEmpty(productFormList);
        if(!errorMessage.equals("")){
            return "Given rows have empty field "+errorMessage;
        }
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

    private  String checkEmpty(List<ProductForm> productFormList){
        String errorMessage = "";
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            double mrp = productForm.getMrp();
            if (barcode.equals("") || brand.equals("") || category.equals("") || mrp == 0) {
                errorMessage += Integer.toString(i) ;
            }
        }
        return errorMessage;
    }
    private  String checkDuplicates(List<ProductForm> productFormList){
        Set<String> hash_Set = new HashSet<String>();
        String errorMessage = "";
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            double mrp = productForm.getMrp();
            if (hash_Set.contains(barcode)) {
                errorMessage += Integer.toString(i) + " " + barcode ;
            }
            hash_Set.add(barcode);
        }
        return errorMessage;
    }
    private  String checkBrandAndCategory(List<ProductForm> productFormList) {
        String errorMessage = "";
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            BrandPojo exists = brandService.getBrandByNameAndCategory(brand,category);
            if (exists != null) {
                errorMessage += Integer.toString(i) + " " + barcode;
            }
        }
        return errorMessage;
    }
    private  String checkDuplicatesInDatabase(List<ProductForm> productFormList){
        String errorMessage = "";
        int i = 1;
        for(ProductForm productForm: productFormList){
            ++i;
            String barcode = productForm.getBarcode();
            String brand = productForm.getBrand();
            String category = productForm.getCategory();
            ProductPojo exists = getByBarcode(barcode);
            if (exists != null) {
                errorMessage += Integer.toString(i) + " " + barcode;
            }
        }
        return errorMessage;
    }
//    private List<UploadErrorMessage> checkData(List<ProductForm> productFormList) throws ApiException {
//        List<UploadErrorMessage> uploadErrorMessageList = new ArrayList<UploadErrorMessage>();
//        Set<String> hash_Set = new HashSet<String>();
//        int row = 0;
//        for (ProductForm productForm : productFormList) {
//            row++;
//            ProductPojo productPojo = getByBarcode(productForm.getBarcode());
//            String barcode = productForm.getBarcode();
//            String brand = productForm.getBrand();
//            String category = productForm.getCategory();
//            double mrp = productForm.getMrp();
//            if (barcode.equals("") || brand.equals("") || category.equals("") || mrp == 0) {
//                uploadErrorMessageList.add(commonUtils.setError(row, "Fields cant be empty"));
//                continue;
//            }
//
//            BrandPojo brandPojo = brandService.getBrandByNameAndCategory(brand, category);
//            if (hash_Set.contains(barcode)) {
//                uploadErrorMessageList.add(commonUtils.setError(row, "Barcode already exists in the TSV"));
//                continue;
//            }
//            if (productPojo != null) {
//                uploadErrorMessageList.add(commonUtils.setError(row, "Barcode already exists in the Database"));
//                continue;
//            }
//
//            if (brandPojo != null) {
//                uploadErrorMessageList.add(commonUtils.setError(row, "Brand and Category doesn't exists"));
//                continue;
//            }
//            if (mrp < 0) {
//                uploadErrorMessageList.add(commonUtils.setError(row, "MRP should be Positive Value"));
//                continue;
//            }
//            hash_Set.add(barcode);
//        }
//
//        return uploadErrorMessageList;
//    }

}
