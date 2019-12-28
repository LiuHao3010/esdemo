package com.elastic_search.demo.Utils;

public class filterUtil {
    public static String transform(String s){
        String tmp=s;
        tmp=tmp.replace("+","xxplusxx");
        tmp=tmp.replace("-","xxlessxx");
        tmp=tmp.replace("%","xxbfhxx");
        return tmp;
    }
}
