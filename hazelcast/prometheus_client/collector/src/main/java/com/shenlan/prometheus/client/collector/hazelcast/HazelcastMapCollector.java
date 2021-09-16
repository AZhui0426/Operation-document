package com.shenlan.prometheus.client.collector.hazelcast;

import com.hazelcast.core.*;
import com.hazelcast.monitor.LocalMapStats;
import com.shenlan.prometheus.client.collector.hazelcast.factory.HazelcastFactoryCollector;
import io.prometheus.client.GaugeMetricFamily;

import java.util.*;

/**
 * @author guomaofei
 * @date 2021/6/9 16:19
 */
public class HazelcastMapCollector extends HazelcastFactoryCollector<IMap> {

    public HazelcastMapCollector(HazelcastInstance... hazelcastInstances) {
        super.setHazelcastInstances(hazelcastInstances);
    }


    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        for (HazelcastInstance hz :
                super.getHazelcastInstances()) {
            Collection<DistributedObject> objects = hz.getDistributedObjects();
            for (DistributedObject ob :
                    objects) {
                if (ob instanceof IMap) {
                    List<MetricFamilySamples> map = getMetric((IMap) ob, hz.getName());
                    mfs.addAll(map);
                }
            }
        }
        return mfs;
    }

    @Override
    public List<MetricFamilySamples> getMetric(IMap iMap, String hzName) {
        List<MetricFamilySamples> samples = new LinkedList<MetricFamilySamples>();
        LocalMapStats mapStatistics = iMap.getLocalMapStats();
        String mapName = iMap.getName();
        if (mapName == null || mapName.equalsIgnoreCase("")) {
            mapName = "test";
        }
        GaugeMetricFamily mapBackupCount = new GaugeMetricFamily("hz_map_backup_count_total", "hazelcast map backup count", Arrays.asList("map_name", "hazelcast_name"));
        mapBackupCount.addMetric(Arrays.asList(mapName, hzName), mapStatistics.getBackupCount());
        samples.add(mapBackupCount);
        GaugeMetricFamily mapBackupEntryCount = new GaugeMetricFamily("hz_map_backup_entry_count_total", "hazelcast map backup entry count", Arrays.asList("map_name", "hazelcast_name"));
        mapBackupEntryCount.addMetric(Arrays.asList(iMap.getName(), hzName), mapStatistics.getBackupEntryCount());
        samples.add(mapBackupEntryCount);
        GaugeMetricFamily mapBackupEntryMemoryCost = new GaugeMetricFamily("hz_map_backup_entry_memory_cost_bytes_total", "hazelcast map backup entry memory cost", Arrays.asList("map_name", "hazelcast_name"));
        mapBackupEntryMemoryCost.addMetric(Arrays.asList(iMap.getName(), hzName), mapStatistics.getBackupEntryMemoryCost());
        samples.add(mapBackupEntryMemoryCost);
        GaugeMetricFamily mapDirtyEntryCount = new GaugeMetricFamily("hz_map_dirty_entry_count_total", "hazelcast map dirty entry count", Arrays.asList("map_name", "hazelcast_name"));
        mapDirtyEntryCount.addMetric(Arrays.asList(iMap.getName(), hzName), mapStatistics.getDirtyEntryCount());
        samples.add(mapDirtyEntryCount);
        GaugeMetricFamily heapCost = new GaugeMetricFamily("hz_map_heap_cost_bytes_total", "hazelcast map heap cost", Arrays.asList("map_name", "hazelcast_name"));
        heapCost.addMetric(Arrays.asList(iMap.getName(), hzName), mapStatistics.getHeapCost());
        samples.add(heapCost);
        GaugeMetricFamily lockedEntryCount = new GaugeMetricFamily("hz_map_locked_entry_count_total", "hazelcast map LockedEntryCount", Arrays.asList("map_name", "hazelcast_name"));
        lockedEntryCount.addMetric(Arrays.asList(iMap.getName(), hzName), mapStatistics.getLockedEntryCount());
        samples.add(lockedEntryCount);
        GaugeMetricFamily ownedEntryCount = new GaugeMetricFamily("hz_map_owned_entry_count_total", "hazelcast map OwnedEntryCount", Arrays.asList("map_name", "hazelcast_name"));
        ownedEntryCount.addMetric(Arrays.asList(iMap.getName(), hzName), mapStatistics.getOwnedEntryCount());
        samples.add(ownedEntryCount);
        GaugeMetricFamily ownedEntryMemoryCost = new GaugeMetricFamily("hz_map_owned_entry_memory_cost_bytes", "hazelcast map OwnedEntryMemoryCost", Arrays.asList("map_name", "hazelcast_name"));
        ownedEntryMemoryCost.addMetric(Arrays.asList(iMap.getName(), hzName), mapStatistics.getOwnedEntryMemoryCost());
        samples.add(ownedEntryMemoryCost);
        return samples;
    }
}
