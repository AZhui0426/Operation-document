package com.shenlan.prometheus.client.collector.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Partition;
import com.shenlan.prometheus.client.collector.hazelcast.factory.HazelcastFactoryCollector;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

import java.util.*;

/**
 * @author guomaofei
 * @date 2021/6/10 14:01
 */
public class HazelcastPartitionCollector extends Collector {

    private HazelcastInstance[] hazelcastInstances;

    public HazelcastPartitionCollector(HazelcastInstance... hazelcastInstances) {
        this.hazelcastInstances = hazelcastInstances;
    }

    private HashMap<String, Integer> partitionSize = new HashMap<String, Integer>();

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> metricFamilySamples = new LinkedList<MetricFamilySamples>();
        for (HazelcastInstance hz :
                hazelcastInstances) {
            Set<Partition> partitions = hz.getPartitionService().getPartitions();
            for (Partition partition :
                    partitions) {
                String address = partition.getOwner().getAddress().toString();
                getAddressMetric(address);
            }
            for (Map.Entry<String, Integer> par :
                    partitionSize.entrySet()) {
                GaugeMetricFamily hzPartitions = new GaugeMetricFamily("hz_partition_total", "hazelcast partition", Arrays.asList(
                        "address", "hazelcast_name"));
                hzPartitions.addMetric(Arrays.asList(par.getKey(), hz.getName()), par.getValue());
                metricFamilySamples.add(hzPartitions);
            }
        }
        partitionSize.clear();
        return metricFamilySamples;
    }


    public void getAddressMetric(String address) {
        if (partitionSize.containsKey(address)) {
            Integer size = partitionSize.get(address);
            size++;
            partitionSize.put(address, size);
        } else {
            Integer size = 1;
            partitionSize.put(address, size);
        }
    }
}