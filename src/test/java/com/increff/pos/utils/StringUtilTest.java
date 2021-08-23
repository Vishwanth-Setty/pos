package com.increff.pos.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

    @Test
    public void testIsEmpty(){
        String string = "";
        assertTrue(StringUtil.isEmpty(string));
        string = null;
        assertTrue(StringUtil.isEmpty(string));
    }

    @Test
    public void testToLowerCase(){
        String string = " aBCd ";
        assertEquals("abcd",StringUtil.toLowerCase(string));
    }
}
