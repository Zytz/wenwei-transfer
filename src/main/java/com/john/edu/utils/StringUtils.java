package com.john.edu.utils;

/**
 * @author:wenwei
 * @date:2020/03/07
 * @description:
 */
public class StringUtils {
    public static String toCamelKey(String key){
//        char[] chars = key.toCharArray();
        StringBuilder buf = null;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if(i==0){

                 ch = Character.toLowerCase(ch);
            }
            buf.append(ch);
        }
        return buf.toString();
    }
}
