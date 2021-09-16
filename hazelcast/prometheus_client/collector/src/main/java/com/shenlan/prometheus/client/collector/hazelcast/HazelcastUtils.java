package com.shenlan.prometheus.client.collector.hazelcast;

import com.hazelcast.core.HazelcastInstance;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author guomaofei
 * @date 2021/6/10 11:24
 */
public class HazelcastUtils {

    public static String getAddress(HazelcastInstance hz) {
        String address = hz.getConfig().getNetworkConfig().getJoin().getTcpIpConfig().getRequiredMember();
        if (address == null || address.equalsIgnoreCase("")) {
            try {
                InetAddress ip4 = Inet4Address.getLocalHost();
                address = ip4.getHostAddress() + ":" + hz.getConfig().getNetworkConfig().getPort();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            address = address + ":" + hz.getConfig().getNetworkConfig().getPort();
        }
        return address;
    }
}
