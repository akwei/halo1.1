package halo.util;

/**
 * 算法思想来自去哪网孙立，查看54chen相关blog <a
 * href="http://www.54chen.com/java-ee/chang-java-treemap-store-ip.html"
 * >http://www.54chen.com/java-ee/chang-java-treemap-store-ip.html</a>
 * 
 * @author akwei
 */
public class IpEntry implements Comparable<IpEntry> {

    private long begin;

    private long end;

    public IpEntry(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public int compareTo(IpEntry o) {
        long begin_amount = this.begin - o.begin;
        if (begin_amount < 0) {
            return -1;
        }
        long end_amount = this.end - o.end;
        if (begin_amount >= 0 && end_amount <= 0) {
            return 0;
        }
        return 1;
    }
}