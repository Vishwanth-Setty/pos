package com.increff.pos.utils;

import com.increff.pos.service.ApiException;

//TODO rename to AbstractApi
public class ErrorUtils {
    public void checkNotNull(Object object,String message) throws ApiException{
        if(object == null){
            throw new ApiException(message);
        }
    }
    public void checkNull(Object object,String message) throws ApiException{
        if(object != null){
            throw new ApiException(message);
        }
    }

}
