package com.vincler.jf.projet6.utils;

import java.util.Random;

public class GetStringUtils {
    public static String getFirstWord(String text) {
        return text.substring(0, text.indexOf(" ", 0));
    }

    public static String random(int size) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < size; i++) {
            tempChar = (char) (generator.nextInt(26) + 65);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}






