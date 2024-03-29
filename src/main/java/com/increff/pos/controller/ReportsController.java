package com.increff.pos.controller;


import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.ReportForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.List;

@Api
@RequestMapping(value="/api/report")
@RestController
public class ReportsController {

    @Autowired
    ReportDto reportDto;

    @ApiOperation(value = "Sales Report")
    @RequestMapping(path = "/sales",method = RequestMethod.POST)
    public List<SalesReportData> generateSalesReport(@RequestBody ReportForm reportForm) throws ApiException {
        return reportDto.getSalesReport(reportForm);
    }

}
