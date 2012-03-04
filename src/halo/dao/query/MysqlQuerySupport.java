package halo.dao.query;

import halo.dao.partition.DaoIdentifier;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * mysql处理作为基础类，以此为扩展可以扩展到db2 oracle等
 * 
 * @author akwei
 */
public class MysqlQuerySupport extends BaseQuerySupport implements
		QuerySupport, DaoIdentifier {

	@Override
	public String getIdentifier() {
		return DaoIdentifier.IDENTIFIER_MYSQL;
	}

	@Override
	public <T> List<T> getListBySQL(String sql, Object[] values, int begin,
			int size, RowMapper<T> rm) {
		if (begin >= 0 && size > 0) {
			return this.getListBySQL(sql + " limit " + begin + "," + size,
					values, rm);
		}
		return this.getListBySQL(sql, values, rm);
	}

	@Override
	public <T> T getObjectBySQL(String sql, Object[] values, RowMapper<T> rm) {
		List<T> list = this.getListBySQL(sql, values, 0, 1, rm);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
}