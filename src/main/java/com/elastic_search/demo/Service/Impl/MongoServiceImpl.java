package com.elastic_search.demo.Service.Impl;

import com.elastic_search.demo.Entity.PcLog2019;
import com.elastic_search.demo.Service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MongoServiceImpl implements MongoService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public void getLog() {
        Query query=new Query();
//        PcLog2019 result=mongoTemplate.findOne(query,PcLog2019.class,"PcLog2018");
        List result=mongoTemplate.findAll(PcLog2019.class,"PcLog2016");
        System.out.println("读取到"+result.size()+"条数据");
//        return Arrays.asList(result);
//        CloseableIterator<test> iterator=mongoTemplate.stream(query, test.class,"test");
//        int i=0;
//        while (iterator.hasNext()){
//            i++;
//            log=iterator.next();
//            System.out.println(log);
//            System.out.println(i);
//        }
//        System.out.println("finsh"+"   "+i);
    }
}
