package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadErrorMessage {
    int rowNumber;
    String error;
}
