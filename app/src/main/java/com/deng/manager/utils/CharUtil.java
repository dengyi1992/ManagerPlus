package com.deng.manager.utils;

/**
 * Created by deng on 16-5-12.
 */
public class CharUtil {
    public static boolean is_number(String number) {
        if(number==null) return false;
        return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
    }

    public boolean is_alpha(String alpha) {
        if(alpha==null) return false;
        return alpha.matches("[a-zA-Z]+");
    }

    public boolean is_chinese(String chineseContent) {
        if(chineseContent==null) return false;
        return chineseContent.matches("[\u4e00-\u9fa5]");
    }

}
