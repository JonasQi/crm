package com.jqcool.crm.utils;


import com.jqcool.crm.exceptions.ParamsException;

public class AssertUtil {


    public static void isTrue(Boolean flag, String msg) {
        if (flag) {
            throw new ParamsException(msg);
        }
    }

}
