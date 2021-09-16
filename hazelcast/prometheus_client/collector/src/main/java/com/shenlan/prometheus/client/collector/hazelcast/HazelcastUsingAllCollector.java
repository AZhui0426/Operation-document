package com.shenlan.prometheus.client.collector.hazelcast;

import com.hazelcast.core.HazelcastInstance;

/**
 * @author guomaofei
 * @date 2021/6/11 11:17
 */
public class HazelcastUsingAllCollector {
    private HazelcastInstance[] hazelcastInstances;

    public HazelcastUsingAllCollector(HazelcastInstance... hazelcastInstances) {
        this.hazelcastInstances = hazelcastInstances;
    }

    public void startAll() {

        new HazelcastClusterCollector(hazelcastInstances).register();
        new HazelcastMapCollector(hazelcastInstances).register();
        new HazelcastTopicCollector(hazelcastInstances).register();
        new HazelcastPartitionCollector(hazelcastInstances).register();
        new JvmCollector(hazelcastInstances).register();
    }
}
