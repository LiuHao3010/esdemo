package com.elastic_search.demo.Service;

import org.springframework.stereotype.Service;

import java.util.List;
public interface EsService {
    public List searchByURL(String s);
    public List findSuspiciousIp();
}
