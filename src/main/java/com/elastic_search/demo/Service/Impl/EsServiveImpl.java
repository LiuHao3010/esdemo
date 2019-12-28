package com.elastic_search.demo.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.elastic_search.demo.Entity.Ipresponse;
import com.elastic_search.demo.Entity.response;
import com.elastic_search.demo.Service.EsService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EsServiveImpl implements EsService {
    @Autowired
    RestHighLevelClient client;
    @Override
    public List searchByURL(String s) {
        List<response> list = new ArrayList<>();
        long begin=System.currentTimeMillis();
        try {
            SearchRequest request=new SearchRequest("weblog");
            SearchSourceBuilder builder=new SearchSourceBuilder();
            builder.size(10000);
            MatchQueryBuilder matchQueryBuilder= QueryBuilders.matchQuery("URL",s);
            builder.query(matchQueryBuilder);
            request.source(builder);
            SearchResponse response=client.search(request,RequestOptions.DEFAULT);
            if (response.getHits().getHits().length==0){
                System.out.println("match query return nothing,use wildcard query,total time: "+(System.currentTimeMillis()-begin));
                WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("URL", "*"+s+"*");
                builder.query(wildcardQueryBuilder);
                request.source(builder);
                response = client.search(request, RequestOptions.DEFAULT);
            }
            else
                System.out.println("match query found!total time: "+(System.currentTimeMillis()-begin));
            for (SearchHit hit:response.getHits().getHits()) {
                list.add(JSON.parseObject(hit.getSourceAsString(), com.elastic_search.demo.Entity.Searchresponse.class));
            }
            return list;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List findSuspiciousIp() {
        long time=System.currentTimeMillis();
        List<Ipresponse> list=new ArrayList();
        SearchRequest request=new SearchRequest("weblog");
        SearchSourceBuilder builder=new SearchSourceBuilder().size(1);
        AggregationBuilder Ipaggsbuilder= AggregationBuilders.terms("mostIp").field("IP").size(100);
        AggregationBuilder SchoolIdfindAggsbuilder=AggregationBuilders.terms("mostSchoolId").field("schoolId").size(100000);
        AggregationBuilder SchoolIdcountAggsbuilder= AggregationBuilders.cardinality("mostSchoolIdCount").field("schoolId");
        Ipaggsbuilder.subAggregation(SchoolIdcountAggsbuilder);
        Ipaggsbuilder.subAggregation(SchoolIdfindAggsbuilder);
        ((TermsAggregationBuilder) Ipaggsbuilder).collectMode();
        builder.aggregation(Ipaggsbuilder);
        request.source(builder);
        try {
            SearchResponse response=client.search(request,RequestOptions.DEFAULT);
            Aggregations aggregations=response.getAggregations();
            Terms terms=aggregations.get("mostIp");
            terms.getBuckets().forEach(bucket->{
                Ipresponse ipresponse=new Ipresponse();
                ipresponse.setChcektime(time);
                List<String> schoolIds=new ArrayList<String>();
//                System.out.println("IP:"+bucket.getKeyAsString()+"-----count:"+bucket.getDocCount());
                ipresponse.setIP(bucket.getKeyAsString());
                ipresponse.setIPcount(bucket.getDocCount());
                Cardinality ts=bucket.getAggregations().get("mostSchoolIdCount");
                ipresponse.setSchoolIdcount(ts.getValue());
//                System.out.println("visited SchoolID's count:"+ts.getValue());
                Terms schoolIdterm=bucket.getAggregations().get("mostSchoolId");
                schoolIdterm.getBuckets().forEach(schoolbucket->{
                    schoolIds.add(schoolbucket.getKeyAsString());
                });
                ipresponse.setSchoolId(schoolIds);
                System.out.println(ipresponse);
                list.add(ipresponse);
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        list.sort((l1,l2)->{
            if(l1.getSchoolIdcount()>l2.getSchoolIdcount())
                return -1;
            else if(l1.getSchoolIdcount()==l2.getSchoolIdcount())
                return 0;
            else
                return 1;

        });
        List<Ipresponse> res=list.stream().filter(c->{
            if(c.getSchoolIdcount()<2)
                return false;
            else
                return true;
        }).collect(Collectors.toList());
        BulkRequest bulkRequest=new BulkRequest();
        res.forEach(c->{
            Map s=new HashMap();
            s.put("chcektime",c.getChcektime());
            s.put("IP",c.getIP());
            s.put("schoolId",c.getSchoolId());
            s.put("schoolIdcount",c.getSchoolIdcount());
            s.put("IPcount",c.getIPcount());
            bulkRequest.add(new IndexRequest("suspiciousip").opType("index").source(s).id(c.getIP().hashCode()+""));
        });
        try {
            System.out.println("update has failure: " + client.bulk(bulkRequest, RequestOptions.DEFAULT).hasFailures());
        }
        catch (Exception e){
            System.out.println("Exception found");
        }
        return res;
    }
}
