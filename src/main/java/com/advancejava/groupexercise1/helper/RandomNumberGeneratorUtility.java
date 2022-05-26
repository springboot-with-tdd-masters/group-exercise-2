package com.advancejava.groupexercise1.helper;

import java.util.Random;

public class RandomNumberGeneratorUtility {


    public static String generate() {
        Random r = new Random();
        Integer low = 100000;
        Integer high = 999999;
        Integer result = r.nextInt(high-low) + low;

        return result.toString();
    }
}
