package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RequestMapping(value="/api/product")
@RestController
public class ProductController {

    @Autowired
    ProductDto productDto;

    @ApiOperation(value = "Creating new Product")
    @RequestMapping(path = "",method = RequestMethod.POST)
    public void add(@RequestBody ProductForm productForm) throws ApiException{
        productDto.add(productForm);
    }

    @ApiOperation(value = "Get all Product")
    @RequestMapping(path = "",method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException{
        return productDto.getAll();
    }

    @ApiOperation(value = "Get Product by Id")
    @RequestMapping(path = "/{id}",method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException{
        return productDto.getById(id);
    }

    @ApiOperation(value = "Get Product by barcode")
    @RequestMapping(path = "/barcode/{barcode}",method = RequestMethod.GET)
    public ProductData getByBarcode(@PathVariable String barcode) throws ApiException{
        return productDto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Update Product")
    @RequestMapping(path = "/{id}",method = RequestMethod.PUT)
    public void update(@PathVariable int id,@RequestBody ProductForm productForm) throws ApiException{
        productDto.update(productForm,id);
    }

    @ApiOperation(value = "UpLoad Product")
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public void upload(@RequestBody List<ProductForm> productFormList) throws ApiException{
        productDto.upload(productFormList);
    }
}
