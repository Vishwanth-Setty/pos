package com.increff.pos.controller;


import com.increff.pos.pdfGenerator.PdfGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

@Api
@RequestMapping(value="/api/report")
@RestController
public class ReportsController {

    PdfGenerator pdfGenerator;

    @ApiOperation(value = "Creating new Product")
    @RequestMapping(path = "/brand",method = RequestMethod.GET)
    @ResponseBody
    public void generateBrandReport(HttpServletResponse response) throws Exception {
        File brandXML = new File("brand.xml");
        File brandXSL = new File("brand.xsl");
        System.out.println("getring dtaa");
        StreamSource xslStreamSource = new StreamSource(brandXSL);
        byte[] pdf = pdfGenerator.generatePDF(brandXML,xslStreamSource);
        System.out.println("getring dtaa");
        System.out.println(Arrays.toString(pdf));

        DataOutputStream os = new DataOutputStream(response.getOutputStream());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "brand.pdf" + "\"");
        byte[] buffer = new byte[1024];
        os.write(pdf);
    }
}
