package com.managedormitory.utils;

@FunctionalInterface
public interface WriteDataLines<T> {
    void writeDataLinesForObject(T t);
}
