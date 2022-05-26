package com.advancejava.groupexercise.helper;

import com.advancejava.groupexercise.model.CustomEntityAudit;

import java.util.Random;

public abstract class RandomNumberGeneratorUtility extends CustomEntityAudit {

    public static String generate() {
        Random r = new Random();
        Integer low =   100000000;
        Integer high =  999999999;
        Integer result = r.nextInt(high-low) + low;

        return result.toString();
    }
}
