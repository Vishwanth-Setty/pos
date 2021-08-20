package com.increff.pos.dto;

import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.ReportForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportDto {

    @Autowired
    ReportService reportService;

    public List<SalesReportData> getSalesReport(ReportForm reportForm) throws ApiException {
        return reportService.getSalesReport(reportForm);
    }
}
