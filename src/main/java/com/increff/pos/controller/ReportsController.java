package com.increff.pos.controller;


import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.ReportForm;
import com.increff.pos.pdfGenerator.PdfGenerator;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Api
@RequestMapping(value="/api/report")
@RestController
public class ReportsController {

    @Autowired
    ReportDto reportDto;

    PdfGenerator pdfGenerator;

    @ApiOperation(value = "Sales Report")
    @RequestMapping(path = "/sales",method = RequestMethod.POST)
    public List<SalesReportData> generateSalesReport(@RequestBody ReportForm reportForm) throws ApiException {
        return reportDto.getSalesReport(reportForm);
    }

    @ApiOperation(value = "Sales Report")
    @RequestMapping(path = "/invoice/{id}",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable int id) throws Exception {

        System.out.println("qwe");
        File xml = new File("../pdfGenerator/brand.xml");
        File xls = new File("../pdfGenerator/brand.xls");
        System.out.println(xml);
        System.out.println(xls);
        StreamSource streamSource = new StreamSource(xls);
        ByteArrayInputStream bis = new ByteArrayInputStream(pdfGenerator.generatePDF(xml,streamSource));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
