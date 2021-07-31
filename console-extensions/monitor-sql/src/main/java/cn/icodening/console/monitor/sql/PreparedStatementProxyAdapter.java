package cn.icodening.console.monitor.sql;

import cn.icodening.console.monitor.sql.define.StatementInfo;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * @author icodening
 * @date 2021.07.28
 */
public abstract class PreparedStatementProxyAdapter extends StatementProxy implements PreparedStatement, ProxyInstance<StatementInfo> {

    private StatementInfo statementInfo;

    public PreparedStatementProxyAdapter(PreparedStatement preparedStatement) {
        super(preparedStatement);
    }

    public PreparedStatement getPreparedStatement() {
        return (PreparedStatement) super.getRaw();
    }

    protected void setParameter(int parameterIndex, Object parameter) {
        StatementInfo dynamicField = getDynamicField();
        dynamicField.getParameters().add(parameterIndex - 1, parameter);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return getPreparedStatement().executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return getPreparedStatement().executeUpdate();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        getPreparedStatement().setNull(parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        getPreparedStatement().setBoolean(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        getPreparedStatement().setByte(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        getPreparedStatement().setShort(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        getPreparedStatement().setInt(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        getPreparedStatement().setLong(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        getPreparedStatement().setFloat(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        getPreparedStatement().setDouble(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        getPreparedStatement().setBigDecimal(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        getPreparedStatement().setString(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        getPreparedStatement().setBytes(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        getPreparedStatement().setDate(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        getPreparedStatement().setTime(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        getPreparedStatement().setTimestamp(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        getPreparedStatement().setAsciiStream(parameterIndex, x, length);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        getPreparedStatement().setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        getPreparedStatement().setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void clearParameters() throws SQLException {
        getPreparedStatement().clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        getPreparedStatement().setObject(parameterIndex, x, targetSqlType);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        getPreparedStatement().setObject(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException {
        return getPreparedStatement().execute();
    }

    @Override
    public void addBatch() throws SQLException {
        getPreparedStatement().addBatch();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        getPreparedStatement().setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        getPreparedStatement().setRef(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        getPreparedStatement().setBlob(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        getPreparedStatement().setClob(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        getPreparedStatement().setArray(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return getPreparedStatement().getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        getPreparedStatement().setDate(parameterIndex, x, cal);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        getPreparedStatement().setTime(parameterIndex, x, cal);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        getPreparedStatement().setTimestamp(parameterIndex, x, cal);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        getPreparedStatement().setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        getPreparedStatement().setURL(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return getPreparedStatement().getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        getPreparedStatement().setRowId(parameterIndex, x);
        setParameter(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        getPreparedStatement().setNString(parameterIndex, value);
        setParameter(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        getPreparedStatement().setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        getPreparedStatement().setNClob(parameterIndex, value);
        setParameter(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        getPreparedStatement().setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        getPreparedStatement().setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        getPreparedStatement().setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        getPreparedStatement().setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        getPreparedStatement().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        getPreparedStatement().setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        getPreparedStatement().setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        getPreparedStatement().setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        getPreparedStatement().setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        getPreparedStatement().setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        getPreparedStatement().setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        getPreparedStatement().setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        getPreparedStatement().setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        getPreparedStatement().setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        getPreparedStatement().setNClob(parameterIndex, reader);
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        getPreparedStatement().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        getPreparedStatement().setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        return getPreparedStatement().executeLargeUpdate();
    }

    @Override
    public void setDynamicField(StatementInfo field) {
        this.statementInfo = field;
    }

    @Override
    public StatementInfo getDynamicField() {
        return statementInfo;
    }
}
