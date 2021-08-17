package com.increff.pos.utils;

import com.increff.pos.service.ApiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public class ValidateUtils<T> {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    public void checkNull(Object object, String message) throws ApiException{
        if(object == null){
            throw new ApiException(message);
        }
    }
    public void checkNotNull(Object object, String message) throws ApiException{
        if(object != null){
            throw new ApiException(message);
        }
    }
    public void checkValid(T object) throws ApiException {
        Set<ConstraintViolation<T>> violations = validator.validate(object) ;
        if(violations.size()>0){
            throw new ApiException("Some fields are empty");
        }
    }
    public void checkValidList(List<T> objects) throws ApiException {
        for(T object:objects){
            checkValid(object);
        }
    }

}
