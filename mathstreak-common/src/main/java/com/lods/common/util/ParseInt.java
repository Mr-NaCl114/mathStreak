package com.lods.common.util;

public class ParseInt {
    public Integer parseInt(String value) {
        return value == null ? null : Integer.valueOf(value);
    }
}
