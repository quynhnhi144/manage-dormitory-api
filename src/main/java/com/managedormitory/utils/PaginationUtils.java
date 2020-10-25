package com.managedormitory.utils;

public class PaginationUtils {
    private PaginationUtils(){}

    public static int getLastElement(int skip, int take, int total){
        int lastElement;
        if (take < total) {
            if (skip + take < total) {
                lastElement = skip + take;
            } else {
                lastElement = total;
            }
        } else {
            lastElement = total;
        }
        return lastElement;
    }
}
