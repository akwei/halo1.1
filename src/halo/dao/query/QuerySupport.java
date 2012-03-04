package halo.dao.query;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public interface QuerySupport {

	/**
	 * 创建记录到数据库
	 * 
	 * @param sql
	 *            sql语句
	 * @param values
	 *            传递的参数
	 * @return 根据不同类型数据库的配置，返回不同的值。例如mysql返回自増id。
	 */
	Object insertBySQL(String sql, Object[] values);

	/**
	 * 查询数据库
	 * 
	 * @param <T>
	 *            查询对象类型
	 * @param sql
	 *            sql语句
	 * @param begin
	 *            开始查询记录号
	 * @param size
	 *            查询记录数量
	 * @param rm
	 *            rowmapper
	 * @param values
	 *            传递的参数
	 * @return 查询对象集合
	 */
	<T> List<T> getListBySQL(String sql, Object[] values, int begin, int size,
			RowMapper<T> rm);

	/**
	 * 查询数据库
	 * 
	 * @param <T>
	 *            查询对象类型
	 * @param sql
	 *            sql语句
	 * @param values
	 *            传递的参数
	 * @param rm
	 *            rowmapper
	 * @return 查询对象集合
	 */
	<T> List<T> getListBySQL(String sql, Object[] values, RowMapper<T> rm);

	/**
	 * 查询数字结果
	 * 
	 * @param sql
	 *            sql语句
	 * @param values
	 *            传递参数
	 * @return 数字结果
	 */
	Number getNumberBySQL(String sql, Object[] values);

	/**
	 * 查询单条记录
	 * 
	 * @param <T>
	 *            对象类型
	 * @param sql
	 *            sql语句
	 * @param values
	 *            传递参数
	 * @param rm
	 *            rowmapper
	 * @return
	 */
	<T> T getObjectBySQL(String sql, Object[] values, RowMapper<T> rm);

	/**
	 * 更新记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param values
	 *            传递参数
	 * @return 返回更新记录的数量
	 */
	int updateBySQL(String sql, Object[] values);
}
