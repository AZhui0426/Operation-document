package com.shenlan.prometheus.client.collector.hazelcast.factory;

import com.hazelcast.core.HazelcastInstance;
import io.prometheus.client.Collector;

import java.util.List;

/**
 * @author guomaofei
 * @date 2021/6/10 14:09
 */
public abstract class HazelcastFactoryCollector<T> extends Collector {


    private HazelcastInstance[] hazelcastInstances;

    /**
     * 获取metricsSamples
     * @param t
     * @return
     */
    public abstract List<MetricFamilySamples> getMetric(T t, String hzName);

    public HazelcastInstance[] getHazelcastInstances() {
        return hazelcastInstances;
    }

    public void setHazelcastInstances(HazelcastInstance[] hazelcastInstances) {
        this.hazelcastInstances = hazelcastInstances;
    }
}
