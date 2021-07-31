package cn.icodening.console.monitor.sql.define;

import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.07.30
 */
public class StatementInfo {

    private String sql;

    private List<Object> parameters = new ArrayList<>();

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
}
