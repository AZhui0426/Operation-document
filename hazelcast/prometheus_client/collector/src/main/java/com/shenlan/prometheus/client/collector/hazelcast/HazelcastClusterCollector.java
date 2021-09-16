package com.shenlan.prometheus.client.collector.hazelcast;

import com.hazelcast.cluster.ClusterState;
import com.hazelcast.core.*;
import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;

import java.util.*;

/**
 * @author guomaofei
 * @date 2021/6/9 16:17
 */
public class HazelcastClusterCollector extends Collector {

    private HazelcastInstance[] hazelcastInstances;

    public HazelcastClusterCollector(HazelcastInstance... hazelcastInstances) {
        this.hazelcastInstances = hazelcastInstances;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        for (HazelcastInstance hz :
                hazelcastInstances) {
            List<MetricFamilySamples> clusterMeters = getClusterMetric(hz);
            mfs.addAll(clusterMeters);
        }
        return mfs;
    }

    public List<MetricFamilySamples> getClusterMetric(HazelcastInstance hz) {
        List<MetricFamilySamples> samples = new LinkedList<MetricFamilySamples>();
        Cluster cluster = hz.getCluster();
        CounterMetricFamily hzClusterInfo = new CounterMetricFamily("hz_cluster_info", "hazelcast cluster", Arrays.asList("master_address",
                "is_cluster_safe", "hazelcast_name"));
        GaugeMetricFamily hzClusterInfoClientSize = new GaugeMetricFamily("hz_cluster_info_client_size_total", "hazelcast cluster client size", Arrays.asList("master_address", "hazelcast_name"));
        GaugeMetricFamily hzClusterInfoMemberSize = new GaugeMetricFamily("hz_cluster_info_member_size_total", "hazelcast cluster member size", Arrays.asList("master_address", "hazelcast_name"));
        Set<Member> members = cluster.getMembers();
        int i = 0;
        for (Member member : members) {
            ++i;
            List<MetricFamilySamples> metricFamilySamples = getMemberInfo(member, hz.getName(), i);
            samples.addAll(metricFamilySamples);
        }
        Member master = members.iterator().next();
        ClusterState clusterState = cluster.getClusterState();
        ClientService clientService = hz.getClientService();
        Collection<Client> clients = clientService.getConnectedClients();
        hzClusterInfo.addMetric(Arrays.asList(master.getAddress().toString(), clusterState.name(), hz.getName()), 0);
        samples.add(hzClusterInfo);
        hzClusterInfoClientSize.addMetric(Arrays.asList(master.getAddress().toString(), hz.getName()), clients.size());
        samples.add(hzClusterInfoClientSize);
        hzClusterInfoMemberSize.addMetric(Arrays.asList(master.getAddress().toString(), hz.getName()), members.size());
        samples.add(hzClusterInfoMemberSize);
        return samples;
    }

    public List<MetricFamilySamples> getMemberInfo(Member member, String hazelcastName, int sNo) {
        List<MetricFamilySamples> samples = new LinkedList<MetricFamilySamples>();
        GaugeMetricFamily memberSNo = new GaugeMetricFamily("hz_member_s_no_info_total", "hazelcast member S.NO", Arrays.asList("member_address", "hazelcast_name"));
        memberSNo.addMetric(Arrays.asList(member.getAddress().toString(), hazelcastName), sNo);
        samples.add(memberSNo);
        return samples;
    }
}
