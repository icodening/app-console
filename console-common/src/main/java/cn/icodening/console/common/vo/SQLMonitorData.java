package cn.icodening.console.common.vo;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author icodening
 * @date 2021.08.01
 */
public class SQLMonitorData {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0);

    public SQLMonitorData() {
        this.id = ATOMIC_LONG.incrementAndGet();
    }

    private final long id;

    private String sql;

    private List<Object> parameters;

    private String jdbcUrl;

    private long costTime;

    public long getId() {
        return id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }
}
