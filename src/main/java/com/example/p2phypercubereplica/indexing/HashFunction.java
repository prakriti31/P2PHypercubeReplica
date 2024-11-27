package com.example.p2phypercubereplica.indexing;

public class HashFunction {

    public static int hash(String input) {
        return Math.abs(input.hashCode());
    }
}
