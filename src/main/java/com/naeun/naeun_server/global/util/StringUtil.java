package com.naeun.naeun_server.global.util;

import java.util.ArrayList;
import java.util.Arrays;

public class StringUtil {
    public static ArrayList<String> getUnderlying(String underlying) {
        underlying = underlying.replace(" ", "").replace("\n", "").replace("\r", "");
        String[] result = underlying.split(",");

        return new ArrayList<>(Arrays.asList(result));
    }
}
