package halo.dao.sql;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConnectionProxyImpl implements ConnectionProxy {

	/**
	 * 保存了真正的Connection
	 */
	private final Map<String, Connection> conMap = new HashMap<String, Connection>();

	private boolean autoCommit;

	private int transactionIsolation;

	private int holdability;

	private boolean readOnly;

	/**
	 * 自定义的数据源
	 */
	private HkDataSourceWrapper cloudDataSourceWrapper;

	public ConnectionProxyImpl(HkDataSourceWrapper cloudDataSourceWrapper)
			throws SQLException {
		this.cloudDataSourceWrapper = cloudDataSourceWrapper;
		this.setAutoCommit(true);
	}

	@Override
	public void clearWarnings() throws SQLException {
		this.getCurrentConnection().clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		Collection<Connection> c = this.conMap.values();
		for (Connection con : c) {
			con.close();
		}
		DataSourceStatus.removeCurrentDsKey();
	}

	@Override
	public void commit() throws SQLException {
		Collection<Connection> c = this.conMap.values();
		for (Connection con : c) {
			con.commit();
		}
	}

	@Override
	public Statement createStatement() throws SQLException {
		return this.getCurrentConnection().createStatement();
	}

	@Override
	public Connection getCurrentConnection() {
		String name = DataSourceStatus.getCurrentDsKey();
		Connection con = this.conMap.get(name);
		if (con == null) {
			try {
				con = this.cloudDataSourceWrapper.getCurrentDataSource()
						.getConnection();
				this.initCurrentConnection(con);
				this.conMap.put(name, con);
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return con;
	}

	private void initCurrentConnection(Connection con) throws SQLException {
		con.setAutoCommit(this.getAutoCommit());
		if (this.getTransactionIsolation() != 0) {
			con.setTransactionIsolation(this.getTransactionIsolation());
		}
		con.setHoldability(this.getHoldability());
		con.setReadOnly(this.isReadOnly());
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return this.getCurrentConnection().createStatement(resultSetType,
				resultSetConcurrency);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.getCurrentConnection().createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}

	@Override
	public int getHoldability() throws SQLException {
		return this.holdability;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.getCurrentConnection().getMetaData();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return this.transactionIsolation;
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return this.getCurrentConnection().getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.getCurrentConnection().getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.getCurrentConnection().isClosed();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return this.readOnly;
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return this.getCurrentConnection().nativeSQL(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return this.getCurrentConnection().prepareCall(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return this.getCurrentConnection().prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.getCurrentConnection().prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.getCurrentConnection().prepareStatement(sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return this.getCurrentConnection().prepareStatement(sql,
				autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return this.getCurrentConnection().prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return this.getCurrentConnection().prepareStatement(sql, columnNames);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return this.getCurrentConnection().prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.getCurrentConnection().prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public void rollback() throws SQLException {
		Collection<Connection> c = conMap.values();
		for (Connection con : c) {
			con.rollback();
		}
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.autoCommit = autoCommit;
		Collection<Connection> c = conMap.values();
		for (Connection con : c) {
			con.setAutoCommit(autoCommit);
		}
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		this.getCurrentConnection().setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return this.getCurrentConnection().getCatalog();
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		this.holdability = holdability;
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		this.readOnly = readOnly;
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		this.transactionIsolation = level;
		Collection<Connection> c = conMap.values();
		for (Connection con : c) {
			con.setTransactionIsolation(level);
		}
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		this.getCurrentConnection().setTypeMap(map);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		throw new SQLException("do not support savepoint");
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		throw new SQLException("do not support savepoint");
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		throw new SQLException("do not support savepoint");
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		throw new SQLException("do not support savepoint");
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return this.getCurrentConnection().createArrayOf(typeName, elements);
	}

	@Override
	public Blob createBlob() throws SQLException {
		return this.getCurrentConnection().createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		return this.getCurrentConnection().createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return this.getCurrentConnection().createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return this.getCurrentConnection().createSQLXML();
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return this.getCurrentConnection().createStruct(typeName, attributes);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return this.getCurrentConnection().getClientInfo();
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		Connection con = this.getCurrentConnection();
		return con.getClientInfo(name);
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return this.getCurrentConnection().isValid(timeout);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		this.getCurrentConnection().setClientInfo(properties);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		this.getCurrentConnection().setClientInfo(name, value);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.getCurrentConnection().isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.getCurrentConnection().unwrap(iface);
	}
}