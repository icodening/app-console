package cn.icodening.console.monitor.sql;

import cn.icodening.console.common.vo.SQLMonitorData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.08.01
 */
public class SQLMonitorDataRepository {

    private static final Map<Long, SQLMonitorData> MONITOR_DATA_MAP = new ConcurrentHashMap<>();

    public static void addMonitorData(SQLMonitorData sqlMonitorData) {
        MONITOR_DATA_MAP.put(sqlMonitorData.getId(), sqlMonitorData);
    }

    public static SQLMonitorData getMonitorData(Long id) {
        return MONITOR_DATA_MAP.get(id);
    }

    public static List<SQLMonitorData> getAllMonitorData() {
        return new ArrayList<>(MONITOR_DATA_MAP.values());
    }
}
