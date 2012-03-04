package halo.dao.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

/**
 * DataSource的包装类
 * 
 * @author akwei
 */
public class HkDataSourceWrapper implements DataSource, InitializingBean {

	public static final String DEFAULT_DBKEY = "defaultdbkey";

	private Map<String, DataSource> dataSourceMap;

	private PrintWriter logWriter;

	private int loginTimeout = 3;

	private boolean debugConnection;

	public void setDebugConnection(boolean debugConnection) {
		this.debugConnection = debugConnection;
	}

	public boolean isDebugConnection() {
		return debugConnection;
	}

	public DataSource getCurrentDataSource() {
		DataSource ds = this.dataSourceMap.get(DataSourceStatus
				.getCurrentDsKey());
		if (ds == null) {
			throw new RuntimeException("no datasource forKey [ "
					+ DataSourceStatus.getCurrentDsKey() + " ]");
		}
		return ds;
	}

	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return new ConnectionProxyImpl(this);
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		throw new SQLException("only support getConnection()");
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.logWriter;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return this.loginTimeout;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.logWriter = out;
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.loginTimeout = seconds;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.getCurrentDataSource().isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.getCurrentDataSource().unwrap(iface);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.dataSourceMap.size() == 1) {
			this.dataSourceMap.put(DEFAULT_DBKEY, this.dataSourceMap.values()
					.iterator().next());
		}
	}
}