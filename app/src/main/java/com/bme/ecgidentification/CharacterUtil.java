package com.bme.ecgidentification;

/**
 * Created by sht on 2017/8/29.
 */

public class CharacterUtil {

    public static int[] String2Int(String buffer){
        int[] data = new int[(buffer.length())];
        for (int i = 0; i < buffer.length() - 1; i++) {
            data[i] = buffer.charAt(i);
        }
        return data;
    }

}
