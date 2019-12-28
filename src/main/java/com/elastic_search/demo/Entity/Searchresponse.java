package com.elastic_search.demo.Entity;
import lombok.Data;

import java.util.Date;

@Data
public class Searchresponse extends response {
    String schoolId;
    String URL;
    String uuId;
    String userAgent;
    String timestamp;
    String IP;
}
