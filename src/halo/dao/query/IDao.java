package halo.dao.query;

import java.util.List;

public interface IDao<T> {

	/**
	 * select count 方法
	 * 
	 * @param keyValue
	 *            分表分库的值
	 * @param where
	 *            查询条件 例如 name=?,不需要添加where关键字
	 * @param params
	 *            条件参数
	 * @return select count 结果
	 */
	int count(Object keyValue, String where, Object[] params);

	/**
	 * 非分区模式使用，方法同 {@link IDao#count(Object, String, Object[])}
	 * 
	 * @param where
	 * @param params
	 * @return
	 */
	int count(String where, Object[] params);

	/**
	 * delete from [table] where arg0=? and arg1=? 删除操作
	 * 
	 * @param keyValue
	 *            分表分库的值
	 * @param where
	 *            查询条件 例如 name=?,不需要添加where关键字
	 * @param params
	 *            条件参数
	 * @return 删除的行数
	 */
	int delete(Object keyValue, String where, Object[] params);

	/**
	 * 删除操作(非分区模式使用)
	 * 
	 * @param where
	 *            查询条件 例如 name=?,不需要添加where关键字
	 * @param params
	 *            条件参数
	 * @return 删除的行数
	 */
	int delete(String where, Object[] params);

	/**
	 * 根据id删除(非分区模式使用)
	 * 
	 * @param idValue
	 *            id
	 * @return 删除的记录数
	 */
	int deleteById(Object idValue);

	/**
	 * 根据id删除数据
	 * 
	 * @param keyValue
	 *            分区关键值
	 * @param idValue
	 *            id
	 * @return 删除的记录数
	 */
	int deleteById(Object keyValue, Object idValue);

	/**
	 * 根据id查询对象(非分区模式使用)
	 * 
	 * @param idValue
	 *            id
	 * @return
	 */
	T getById(Object idValue);

	/**
	 * 根据id查询对象
	 * 
	 * @param keyValue
	 *            分区关键值
	 * @param idValue
	 *            id
	 * @return
	 */
	T getById(Object keyValue, Object idValue);

	/**
	 * 根据条件查询
	 * 
	 * @param keyValue
	 *            分区关键值
	 * @param where
	 *            条件sql片段
	 * @param params
	 *            sql对应参数
	 * @param order
	 *            排序sql片段
	 * @param begin
	 *            开始记录位置
	 * @param size
	 *            <0时，取所有符合条件数据
	 * @return 数据集合
	 */
	List<T> getList(Object keyValue, String where, Object[] params,
			String order, int begin, int size);

	/**
	 * 根据条件查询(非分区模式使用)
	 * 
	 * @param where
	 *            条件sql片段
	 * @param params
	 *            sql对应参数
	 * @param order
	 *            排序sql片段
	 * @param begin
	 *            开始记录位置
	 * @param size
	 *            <0时，取所有符合条件数据
	 * @return 数据集合
	 */
	List<T> getList(String where, Object[] params, String order, int begin,
			int size);

	/**
	 *使用in关键字操作，例如：select * from table where field in (v1,v2,v3)
	 * 
	 * @param <E>
	 * @param keyValue
	 *            分区关键值
	 * @param field
	 *            需要用in的字段
	 * @param fieldValueList
	 *            in 的参数
	 * @return 数据集合
	 */
	<E> List<T> getListInField(Object keyValue, String field,
			List<E> fieldValueList);

	/**
	 * 使用in关键字操作，例如：select * from table where field in (v1,v2,v3) and name=?
	 * 
	 * @param <E>
	 * @param keyValue
	 *            分区关键值
	 * @param where
	 *            条件sql片段
	 * @param params
	 *            参数
	 * @param field
	 *            需要用in的字段
	 * @param fieldValueList
	 *            in 的参数
	 * @return 数据集合
	 */
	<E> List<T> getListInField(Object keyValue, String where, Object[] params,
			String field, List<E> fieldValueList);

	/**
	 * 使用in关键字操作，例如：select * from table where field in (v1,v2,v3) (非分区模式使用)
	 * 
	 * @param <E>
	 * @param field
	 *            需要用in的字段
	 * @param fieldValueList
	 *            in 的参数
	 * @return 数据集合
	 */
	<E> List<T> getListInField(String field, List<E> fieldValueList);

	/**
	 * 使用in关键字操作(非分区模式使用)
	 * 
	 * @param <E>
	 * @param where
	 *            条件sql片段
	 * @param params
	 *            参数
	 * @param field
	 *            需要用in的字段
	 * @param fieldValueList
	 *            in 的参数
	 * @return 数据集合
	 */
	<E> List<T> getListInField(String where, Object[] params, String field,
			List<E> fieldValueList);

	/**
	 * 根据条件查询对象
	 * 
	 * @param keyValue
	 *            分区关键值
	 * @param where
	 *            条件sql片段
	 * @param params
	 *            参数
	 * @return 查询结果
	 */
	T getObject(Object keyValue, String where, Object[] params);

	/**
	 * 根据条件查询对象
	 * 
	 * @param keyValue
	 *            分区关键值
	 * @param where
	 *            条件sql片段
	 * @param params
	 *            参数
	 * @param order
	 *            排序sql片段
	 * @return 查询结果
	 */
	T getObject(Object keyValue, String where, Object[] params, String order);

	/**
	 * 根据条件查询对象(非分区模式使用)
	 * 
	 * @param where
	 *            查询条件 例如 name=?,不需要添加where关键字
	 * @param params
	 *            参数
	 * @return 查询结果
	 */
	T getObject(String where, Object[] params);

	/**
	 * 根据条件查询对象(非分区模式使用)
	 * 
	 * @param where
	 *            查询条件 例如 name=?,不需要添加where关键字
	 * @param params
	 *            参数
	 * @param order
	 *            排序sql片段。例如:order by name desc 此参数为name desc
	 * @return 查询结果
	 */
	T getObject(String where, Object[] params, String order);

	/**
	 * 创建对象
	 * 
	 * @param keyValue
	 *            分区关键值
	 * @param t
	 * @return
	 */
	Object save(Object keyValue, T t);

	/**
	 * 非分区模式使用
	 * 
	 * @param t
	 * @return
	 */
	Object save(T t);

	/**
	 * 更新对象
	 * 
	 * @param keyValue
	 *            分区关键值
	 * @param t
	 * @return
	 */
	int update(Object keyValue, T t);

	/**
	 * 非分区模式使用
	 * 
	 * @param t
	 * @return
	 */
	int update(T t);

	int updateBySQL(Object keyValue, String updateSqlSegment, String where,
			Object[] params);

	/**
	 * 非分区模式使用
	 * 
	 * @param updateSqlSegment
	 *            update片段。例如：update sql为update table set name=?,createtime=?
	 *            where userid=?，那么sql片段为name=?,createtime=?
	 * @param where
	 *            查询条件 例如 name=?,不需要添加where关键字
	 * @param params
	 *            参数
	 * @return
	 */
	int updateBySQL(String updateSqlSegment, String where, Object[] params);
}
