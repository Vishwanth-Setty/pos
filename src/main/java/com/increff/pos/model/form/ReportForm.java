package com.increff.pos.model.form;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReportForm {

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

    @NotNull
    private String brand;

    @NotNull
    private String category;

}
