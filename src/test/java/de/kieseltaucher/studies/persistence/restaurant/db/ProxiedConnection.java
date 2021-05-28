package de.kieseltaucher.studies.persistence.restaurant.db;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

class ProxiedConnection implements Connection {

    private final Connection proxied;

    ProxiedConnection(Connection proxied) {
        this.proxied = proxied;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return proxied.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return proxied.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return proxied.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return proxied.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        proxied.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return proxied.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        proxied.commit();
    }

    @Override
    public void rollback() throws SQLException {
        proxied.rollback();
    }

    @Override
    public void close() throws SQLException {
        proxied.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return proxied.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return proxied.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        proxied.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return proxied.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        proxied.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return proxied.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        proxied.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return proxied.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return proxied.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        proxied.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return proxied.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return proxied.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return proxied.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return proxied.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        proxied.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        proxied.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return proxied.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return proxied.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return proxied.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        proxied.rollback();
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        proxied.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return proxied.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return proxied.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return proxied.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return proxied.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return proxied.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return proxied.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return proxied.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return proxied.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return proxied.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return proxied.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return proxied.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        proxied.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        proxied.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return proxied.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return proxied.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return proxied.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return proxied.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        proxied.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return proxied.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        proxied.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        proxied.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return proxied.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return proxied.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return proxied.isWrapperFor(iface);
    }


}
