package com.increff.pos.model.form;


import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BrandForm {

    @NotBlank(message = "Can not be blank")
    private String brand;

    @NotBlank(message = "Can not be blank")
    private String category;

}
