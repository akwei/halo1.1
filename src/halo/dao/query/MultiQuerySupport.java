package halo.dao.query;

import halo.dao.partition.DaoIdentifier;
import halo.dao.sql.DataSourceStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * 对应多种数据库的daoSupport切换使用。数据源必须表明前缀，这样可与daosupport对应
 * 
 * @author akwei
 */
public class MultiQuerySupport implements QuerySupport {

	/**
	 * 配置的多数据库的daosupport
	 */
	private final Map<String, QuerySupport> querySupportMap = new HashMap<String, QuerySupport>();

	/**
	 * 配置的多数据库的daosupport集合
	 */
	private List<QuerySupport> querySupports;

	/**
	 * 把实现DaoIdentifier 接口的类装入map中，便于根据标识获取相应数据库类型的daosupport
	 * 
	 * @param daoSupportList
	 */
	public void setQuerySupports(List<QuerySupport> querySupports) {
		this.querySupports = querySupports;
		for (QuerySupport querySupport : this.querySupports) {
			if (querySupport instanceof DaoIdentifier) {
				DaoIdentifier id = (DaoIdentifier) querySupport;
				String identifier = id.getIdentifier();
				this.querySupportMap.put(identifier, querySupport);
			}
		}
	}

	public String getCurrentDaoSupportIdentifier() {
		String dsName = DataSourceStatus.getCurrentDsKey();
		int idx = dsName.indexOf('_');
		if (idx == -1) {
			return null;
		}
		return dsName.substring(0, idx);
	}

	/**
	 * 根据当前环境设定的数据源特征返回相应的daosupport
	 * 
	 * @return
	 */
	private QuerySupport getQuerySupport() {
		String identifier = this.getCurrentDaoSupportIdentifier();
		if (identifier == null) {
			return this.querySupports.get(0);
		}
		return this.querySupportMap.get(identifier);
	}

	@Override
	public Object insertBySQL(String sql, Object[] values) {
		return this.getQuerySupport().insertBySQL(sql, values);
	}

	@Override
	public <T> List<T> getListBySQL(String sql, Object[] values, int begin,
			int size, RowMapper<T> rm) {
		return this.getQuerySupport()
				.getListBySQL(sql, values, begin, size, rm);
	}

	@Override
	public <T> List<T> getListBySQL(String sql, Object[] values, RowMapper<T> rm) {
		return this.getQuerySupport().getListBySQL(sql, values, rm);
	}

	@Override
	public Number getNumberBySQL(String sql, Object[] values) {
		return this.getQuerySupport().getNumberBySQL(sql, values);
	}

	@Override
	public <T> T getObjectBySQL(String sql, Object[] values, RowMapper<T> rm) {
		return this.getQuerySupport().getObjectBySQL(sql, values, rm);
	}

	@Override
	public int updateBySQL(String sql, Object[] values) {
		return this.getQuerySupport().updateBySQL(sql, values);
	}
}