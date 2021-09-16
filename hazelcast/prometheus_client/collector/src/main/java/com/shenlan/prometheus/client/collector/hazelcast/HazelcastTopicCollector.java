package com.shenlan.prometheus.client.collector.hazelcast;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import com.hazelcast.monitor.LocalTopicStats;
import com.shenlan.prometheus.client.collector.hazelcast.factory.HazelcastFactoryCollector;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;

import java.util.*;

/**
 * @author guomaofei
 * @date 2021/6/10 10:46
 */
public class HazelcastTopicCollector extends HazelcastFactoryCollector<ITopic> {

    private HazelcastInstance[] hazelcastInstances;

    public HazelcastTopicCollector(HazelcastInstance... hazelcastInstances) {
        this.hazelcastInstances = hazelcastInstances;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        for (HazelcastInstance hz :
                hazelcastInstances) {
            Collection<DistributedObject> objects = hz.getDistributedObjects();
            for (DistributedObject ob :
                    objects) {
                if (ob instanceof ITopic) {
                    List<MetricFamilySamples> map = getMetric((ITopic) ob, hz.getName());
                    mfs.addAll(map);
                }
            }
        }
        return mfs;
    }
    
    @Override
    public List<MetricFamilySamples> getMetric(ITopic topic, String hzName) {
        List<MetricFamilySamples> samples = new LinkedList<MetricFamilySamples>();
        LocalTopicStats topicStatistics = topic.getLocalTopicStats();
        CounterMetricFamily topicPublish = new CounterMetricFamily("hz_topic_publish_message_count_total", "total number of published messages of the topic on the member", Arrays.asList("topic_name", "hazelcast_name"));
        topicPublish.addMetric(Arrays.asList(topic.getName(), hzName), topicStatistics.getPublishOperationCount());
        samples.add(topicPublish);
        CounterMetricFamily topicReceive = new CounterMetricFamily("hz_topic_receive_message_count_total", "total number of received messages of the topic on the member", Arrays.asList("topic_name", "hazelcast_name"));
        topicReceive.addMetric(Arrays.asList(topic.getName(), hzName), topicStatistics.getReceiveOperationCount());
        samples.add(topicReceive);
        return samples;
    }
}
