package com.elastic_search.demo.Controller;

import com.elastic_search.demo.Service.MongoService;
import com.elastic_search.demo.Utils.filterUtil;
import com.elastic_search.demo.Service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.List;

@RestController()
public class TestController {
    @Autowired
    EsService esService;
    @Autowired
    MongoService mongoService;
    @RequestMapping("/search")
    public List search(String s){
        s=URLEncoder.encode(s);
        s= filterUtil.transform(s);
        List list=esService.searchByURL(s);
        System.out.println(list.size());
        return list;
    }
    @RequestMapping("/mongotest")
    public void get(){
        mongoService.getLog();
    }
    @RequestMapping("/IPcheck")
    public List IPcheck(){
        return esService.findSuspiciousIp();
    }
}
