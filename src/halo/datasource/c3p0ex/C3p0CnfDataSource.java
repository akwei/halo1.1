package halo.datasource.c3p0ex;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class C3p0CnfDataSource implements DataSource {

	private DataSource dataSource;

	private String dsName;

	private C3p0Cnf c3p0Cnf;

	public C3p0CnfDataSource(C3p0Cnf c3p0Cnf, String dsName) {
		this.c3p0Cnf = c3p0Cnf;
		this.dsName = dsName;
		this.init();
	}

	private void init() {
		this.dataSource = this.c3p0Cnf.createDataSource(this.dsName);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.dataSource.getLogWriter();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return this.dataSource.getLoginTimeout();
	}

	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException {
		this.dataSource.setLogWriter(arg0);
	}

	@Override
	public void setLoginTimeout(int arg0) throws SQLException {
		this.dataSource.setLoginTimeout(arg0);
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}

	@Override
	public Connection getConnection(String arg0, String arg1)
			throws SQLException {
		return this.dataSource.getConnection(arg0, arg1);
	}
}
