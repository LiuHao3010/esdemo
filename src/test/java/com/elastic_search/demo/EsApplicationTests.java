package com.elastic_search.demo;

import com.elastic_search.demo.Utils.IPUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(IPUtil.internalIp("192.168.1.1"));
    }


}
