package com.yih.chasm.net;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EndPoint {
    private String ip;
    private Integer port;

    public EndPoint() {

    }

    public EndPoint(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] argc) {
        EndPoint ep1 = new EndPoint("localhost", 12345);
        EndPoint ep2 = new EndPoint("localhost", 12346);
        EndPoint ep3 = new EndPoint("localhost", 12347);

        Map<EndPoint, String> map = new HashMap<>();

        map.put(ep1, "1");
        map.put(ep2, "1");
        map.put(ep3, "1");

        System.out.println(ep1.hashCode());
        System.out.println(new EndPoint("localhost", 12345).hashCode());
        System.out.println(new EndPoint("localhost", 12345).hashCode());

        System.out.println(ep1.equals(new EndPoint("localhost", 12345)));

        System.out.println(map.containsKey(new EndPoint("localhost", 12345)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EndPoint endPoint = (EndPoint) o;

        if (!ip.equals(endPoint.ip)) return false;
        return port.equals(endPoint.port);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port.hashCode();
        return result;
    }
}
