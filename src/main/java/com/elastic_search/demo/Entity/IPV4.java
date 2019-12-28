package com.elastic_search.demo.Entity;

import com.elastic_search.demo.Utils.IPUtil;
import lombok.Data;

@Data
public class IPV4 {
    int part1;
    int part2;
    int part3;
    int part4;

    public IPV4(int part1, int part2, int part3, int part4) {
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
        this.part4 = part4;
    }

    public static IPV4 getIPFromString(String ip){
        String[] part=ip.split("\\.");
        if(part.length!=4)
            return null;
        else {
            IPV4 res=new IPV4(Integer.parseInt(part[0]),Integer.parseInt(part[1]),Integer.parseInt(part[2]),Integer.parseInt(part[3]));
            if(IPUtil.internalIp(res.toString()))
                return null;
            else
                return res;
        }

    }

    @Override
    public String toString() {
        return  part1 +
                "." + part2 +
                "." + part3 +
                "." + part4 ;
    }
}
