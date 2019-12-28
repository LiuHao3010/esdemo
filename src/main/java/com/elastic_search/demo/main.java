package com.elastic_search.demo;

import com.elastic_search.demo.Entity.IPV4;
import com.elastic_search.demo.Utils.IPUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class main {
    private static AtomicInteger i=new AtomicInteger(0);
    public static void main(String[] args)throws Exception{
        IPV4 ip=IPV4.getIPFromString("92.168.1.1");
        System.out.println(IPUtil.changeIP(ip));
    }
}
