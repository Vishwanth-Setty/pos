package com.increff.pos.controller;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RequestMapping(value="/api/product")
@RestController
public class ProjectController {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @ApiOperation(value = "Creating new Product")
    @RequestMapping(path = "",method = RequestMethod.POST)
    public void addProduct(@RequestBody ProductForm productForm) throws ApiException{
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
        productForm.setBrandId(brandPojo.getId());
        ProductPojo productPojo = convert(productForm);
        productService.addProduct(productPojo);
    }

    @ApiOperation(value = "Get all Product")
    @RequestMapping(path = "",method = RequestMethod.GET)
    public List<ProductData> getAllProducts(){
        List<ProductData> listProductData = productService.getProductsList();
        return listProductData;
    }

    @ApiOperation(value = "Get Product by Id")
    @RequestMapping(path = "/{id}",method = RequestMethod.GET)
    public ProductData getProduct(@PathVariable int id){
        ProductData productData = productService.getProduct(id);
        return productData;
    }
    @ApiOperation(value = "Get Product by barcode")
    @RequestMapping(path = "/barcode/{barcode}",method = RequestMethod.GET)
    public ProductPojo getProductByBarcode(@PathVariable String barcode){
        return productService.getProductByBarcode(barcode);
    }

    @ApiOperation(value = "Update Product")
    @RequestMapping(path = "/{id}",method = RequestMethod.PUT)
    public void updateProduct(@PathVariable int id,@RequestBody ProductForm productForm){
        ProductPojo productPojo = convert(productForm);
        productPojo.setId(id);
        productService.updateProduct(productPojo);
    }


    private static ProductData convert(ProductPojo p){
        ProductData d = new ProductData();
        d.setId(p.getId());
        d.setBarcode(p.getBarcode());
        d.setBrandId(p.getBrandId());
        d.setName(p.getName());
        d.setMrp(p.getMrp());
        return d;
    }

    private static ProductPojo convert(ProductForm form){
        ProductPojo p = new ProductPojo();
        p.setBarcode(form.getBarcode());
        p.setBrandId(form.getBrandId());
        p.setName(form.getName());
        p.setMrp(form.getMrp());
        return p;
    }
}
