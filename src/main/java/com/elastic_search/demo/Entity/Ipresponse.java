package com.elastic_search.demo.Entity;
import lombok.Data;

import java.util.List;

@Data
public class Ipresponse extends response {
    String IP;
    List<String> schoolId;
    Long schoolIdcount;
    Long IPcount;
    Long chcektime;
}
