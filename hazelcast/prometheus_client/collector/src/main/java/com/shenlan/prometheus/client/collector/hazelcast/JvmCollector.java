package com.shenlan.prometheus.client.collector.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author guomaofei
 * @date 2021/6/10 15:38
 */
public class JvmCollector extends Collector {

    private HazelcastInstance[] hazelcastInstances;

    public JvmCollector(HazelcastInstance... hazelcastInstances) {
        this.hazelcastInstances = hazelcastInstances;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> metricFamilySamples = new LinkedList<MetricFamilySamples>();
        for (HazelcastInstance hz :
                hazelcastInstances) {
            //查看jvm空闲内存大小，单位字节
            long freeMemory = Runtime.getRuntime().freeMemory();

            long total = Runtime.getRuntime().totalMemory();
            Member member = hz.getCluster().getLocalMember();
            CounterMetricFamily jvmMemoryTotal = new CounterMetricFamily("jvm_memory_max_bytes", "jvm_memory_max_bytes The maximum amount of memory in bytes that can be used for memory management", Arrays.asList("hazelcast_name", "address"));
            jvmMemoryTotal.addMetric(Arrays.asList(hz.getName(), member.getAddress().toString()), total);
            metricFamilySamples.add(jvmMemoryTotal);
            CounterMetricFamily jvmMemoryFree = new CounterMetricFamily("jvm_memory_free_bytes", "jvm_memory_free_bytes The maximum amount of memory in bytes that can be used for memory management", Arrays.asList("hazelcast_name", "address"));
            jvmMemoryFree.addMetric(Arrays.asList(hz.getName(), member.getAddress().toString()), freeMemory);
            metricFamilySamples.add(jvmMemoryFree);
        }
        return metricFamilySamples;
    }
}
