package halo.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import sun.net.util.IPAddressUtil;

public class IPUtil {

    public static BigInteger getIpNumber(String ip) {
        if (ip == null || ip.trim().length() == 0) {
            throw new IllegalArgumentException("not ip format");
        }
        StringBuilder sb = new StringBuilder("0x");
        if (IPAddressUtil.isIPv4LiteralAddress(ip)) {
            // bys = IPAddressUtil.textToNumericFormatV4(ip);
            String[] v = ip.split("\\.");
            for (int i = 0; i < v.length; i++) {
                sb.append(Long.toHexString(Long.valueOf(v[i])));
            }
            BigInteger b = new BigInteger(String.valueOf(Long.decode(sb
                    .toString())));
            return b;
        }
        else if (IPAddressUtil.isIPv6LiteralAddress(ip)) {
            return processIpv6(ip);
        }
        else {
            throw new IllegalArgumentException("not support ip format");
        }
    }

    private static BigInteger processIpv6(String ip) {
        String _ip = ip.trim();
        if (_ip.equals("::")) {// ip[::]
            return new BigInteger("0");
        }
        int idx = _ip.indexOf("::");
        String ipv6 = _ip;
        // 存在缩写格式的ip
        if (idx != -1) {
            // ip[2001::25de::cade] 非法ipv6
            if (_ip.lastIndexOf("::") != idx) {
                throw new IllegalArgumentException("not supported ipv6 format");
            }
            // // ip[2001::] 末尾::
            if (idx == _ip.length() - 2) {
                _ip = ip + "0000";
                idx = _ip.indexOf("::");
            }
            // 开头
            else if (idx == 0) {
                _ip = "0000" + _ip;
                idx = _ip.indexOf("::");
            }
            // 不在末尾的::
            String v0 = _ip.substring(0, idx);
            String v1 = _ip.substring(idx + 2);
            StringBuilder sbb = new StringBuilder();
            int sum = 0;
            while (sum != 6) {
                sbb.append(":0000");
                sum = countSignal(v0, ":") + countSignal(sbb.toString(), ":")
                        + countSignal(v1, ":");
            }
            ipv6 = v0 + sbb.toString() + ":" + v1;
        }
        // ip[2001:0000:0000:0000:0000:25de:0000:cade] 正规格式
        String[] v = ipv6.split(":");
        List<String> list = new ArrayList<String>();
        for (String s : v) {
            list.add(s);
        }
        list = fmtIpv6SegList(list);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        return new BigInteger(sb.toString(), 16);
    }

    private static List<String> fmtIpv6SegList(List<String> list) {
        List<String> _list = new ArrayList<String>();
        for (String s : list) {
            _list.add(fmtIpv6Seg(s));
        }
        return _list;
    }

    private static String fmtIpv6Seg(String s) {
        int remain = 4 - s.length();
        if (remain == 0) {
            return s;
        }
        if (remain > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < remain; i++) {
                sb.append("0");
            }
            sb.append(s);
            return sb.toString();
        }
        throw new RuntimeException("not support ipv6 format");
    }

    /**
     * 查询子串出现的次数
     * 
     * @param str
     *            字符串
     * @param s
     *            子串
     * @return
     */
    public static int countSignal(String str, String s) {
        int sum = 0;
        int idx = str.indexOf(s);
        while (idx != -1) {
            sum++;
            idx = str.indexOf(s, idx + 1);
        }
        return sum;
    }
}