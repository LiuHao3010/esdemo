package com.elastic_search.demo.Utils;

import com.elastic_search.demo.Entity.IPV4;
import sun.net.util.IPAddressUtil;

public class IPUtil {
    public static IPV4 changeIP(IPV4 source){
        if(source==null)
            return source;
        int p1=source.getPart1();
        int p2=source.getPart2();
        int p3=source.getPart3();
        int p4=source.getPart4()+10;
        IPV4 res=new IPV4(p1,p2,p3,p4);
        if(internalIp(res.toString()))
            return source;
        else
            return res;
    }

    public static boolean internalIp(String ip) {
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
        return internalIp(addr);
    }
    public static boolean internalIp(byte[] addr) {
        if(addr==null)
            return true;
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;

        }
    }
}
