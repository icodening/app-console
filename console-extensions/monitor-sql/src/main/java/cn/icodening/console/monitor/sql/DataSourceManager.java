package cn.icodening.console.monitor.sql;

import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据源管理器，记录多数据源
 *
 * @author icodening
 * @date 2021.07.31
 */
public class DataSourceManager {

    private static final List<DataSource> DATA_SOURCES = new CopyOnWriteArrayList<>();

    private static final Map<String, DataSource> URL_DATASOURCE_MAP = new ConcurrentHashMap<>();

    public static void addDataSource(DataSource dataSource) {
        DATA_SOURCES.add(dataSource);
        if (dataSource instanceof DataSourceProxy) {
            String jdbcUrl = ((DataSourceProxy) dataSource).getDynamicField().getJdbcUrl();
            if (StringUtils.hasText(jdbcUrl)) {
                URL_DATASOURCE_MAP.put(jdbcUrl, dataSource);
            }
        }
    }

    public static List<DataSource> getDataSources() {
        return Collections.unmodifiableList(DATA_SOURCES);
    }

    public static DataSource getDataSource(String jdbcUrl) {
        return URL_DATASOURCE_MAP.get(jdbcUrl);
    }
}
