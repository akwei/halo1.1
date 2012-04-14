package halo.util;

public class IpData {

    private long begin;

    private long end;

    private String name;

    public IpData(long begin, long end, String name) {
        super();
        this.begin = begin;
        this.end = end;
        this.name = name;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}