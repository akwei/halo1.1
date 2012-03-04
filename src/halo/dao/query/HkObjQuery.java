package halo.dao.query;

import halo.util.NumberUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * 对象化处理sql
 * 
 * @author akwei
 */
public class HkObjQuery extends HkQuery {

	public HkObjQuery() {
	}

	private ObjectSqlInfoCreater objectSqlInfoCreater = new ObjectSqlInfoCreater();

	/**
	 * insert into sql 操作
	 * 
	 * @param insertParam
	 * @param t
	 *            需要insert的对象
	 * @return mysql中返回自增id，其他数据库暂时返回null
	 */
	@SuppressWarnings("unchecked")
	public <T> Object insertObj(InsertParam insertParam, T t) {
		ObjectSqlInfo<T> objectSqlInfo = (ObjectSqlInfo<T>) this.objectSqlInfoCreater
				.getObjectSqlInfo(t.getClass());
		PartitionTableInfo partitionTableInfo = this.parse(t.getClass(),
				insertParam.getCtxMap());
		return this.insertBySQL(partitionTableInfo.getDsKey(),
				SqlBuilder.createInsertSQL(partitionTableInfo,
						objectSqlInfo.getColumns()), objectSqlInfo
						.getSqlUpdateMapper().getParamsForInsert(t));
	}

	/**
	 * insert对象
	 * 
	 * @param key
	 *            分区key
	 * @param keyValue
	 *            分区value
	 * @param t
	 *            需要insert的对象
	 * @return mysql中返回自增id,其他数据库中返回值暂时为null
	 */
	public <T> Object insertObj(String key, Object keyValue, T t) {
		InsertParam insertParam = new InsertParam(key, keyValue);
		return this.insertObj(insertParam, t);
	}

	/**
	 * @param key
	 *            分区关键字
	 * @param keyValue
	 *            分区关键字对应的值
	 * @param t
	 *            需要insert的对象
	 * @return mysql中返回自增id
	 */
	public <T> Number insertObjForNumber(String key, Object keyValue, T t) {
		return NumberUtil.getNumber(this.insertObj(key, keyValue, t));
	}

	/**
	 * insert对象
	 * 
	 * @param insertParam
	 *            insert所包含的信息参考 {@link InsertParam}
	 * @param t
	 *            需要insert的对象
	 * @return
	 */
	public <T> Number insertObjForNumber(InsertParam insertParam, T t) {
		return NumberUtil.getNumber(this.insertObj(insertParam, t));
	}

	/**
	 * update sql操作，update一条记录的所有字段
	 * 
	 * @param key
	 *            分区key
	 * @param keyValue
	 *            分区value
	 * @param t
	 *            update 的对象
	 * @return update sql的返回值
	 */
	@SuppressWarnings("unchecked")
	public <T> int updateObj(String key, Object keyValue, T t) {
		ObjectSqlInfo<T> objectSqlInfo = (ObjectSqlInfo<T>) this.objectSqlInfoCreater
				.getObjectSqlInfo(t.getClass());
		UpdateParam updateParam = new UpdateParam(key, keyValue);
		updateParam.setClazz(t.getClass());
		updateParam.set(objectSqlInfo.getColumnsForUpdate(),
				objectSqlInfo.getIdColumn() + "=?", objectSqlInfo
						.getSqlUpdateMapper().getParamsForUpdate(t));
		return this.update(updateParam);
	}

	/**
	 * update sql 操作
	 * 
	 * @param updateParam
	 *            update所包含的信息参考 {@link UpdateParam}
	 * @param clazz
	 *            需要update的对象类型
	 * @return
	 */
	public int update(UpdateParam updateParam) {
		PartitionTableInfo partitionTableInfo = this.parse(
				updateParam.getClazz(), updateParam.getCtxMap());
		return this
				.updateBySQL(partitionTableInfo.getDsKey(), SqlBuilder
						.createUpdateSQL(partitionTableInfo,
								updateParam.getUpdateColumns(),
								updateParam.getWhere()), updateParam
						.getParams());
	}

	public int update(UpdateParamBuilder updateParamBuilder) {
		return this.update(updateParamBuilder.create());
	}

	/**
	 * delete sql操作
	 * 
	 * @param deleteParam
	 *            delete所包含的信息参考 {@link DeleteParam}
	 * @param clazz
	 *            需要delete的对象类型
	 * @return delete sql的返回值
	 */
	public int delete(DeleteParam deleteParam) {
		PartitionTableInfo partitionTableInfo = this.parse(
				deleteParam.getClazz(), deleteParam.getCtxMap());
		return this.updateBySQL(
				partitionTableInfo.getDsKey(),
				SqlBuilder.createDeleteSQL(partitionTableInfo,
						deleteParam.getWhere()), deleteParam.getParams());
	}

	/**
	 * 通过id删除对象
	 * 
	 * @param key
	 *            分区key
	 * @param keyValue
	 *            分区value
	 * @param clazz
	 *            需要delete的对象类型
	 * @param idValue
	 * @return delete sql返回值
	 */
	public <T> int deleteById(String key, Object keyValue, Class<T> clazz,
			Object idValue) {
		Map<String, Object> ctxMap = new HashMap<String, Object>();
		ctxMap.put(key, keyValue);
		return this.deleteById(ctxMap, clazz, idValue);
	}

	/**
	 * 通过id删除对象
	 * 
	 * @param ctxMap
	 *            分区kye-value的map
	 * @param clazz
	 *            需要delete的对象类
	 * @param idValue
	 *            对象id
	 * @return delete sql返回值
	 */
	public <T> int deleteById(Map<String, Object> ctxMap, Class<T> clazz,
			Object idValue) {
		DeleteParam deleteParam = new DeleteParam();
		deleteParam.addKeyAdnValueFromMap(ctxMap);
		deleteParam.setClazz(clazz);
		ObjectSqlInfo<T> objectSqlInfo = this.objectSqlInfoCreater
				.getObjectSqlInfo(clazz);
		deleteParam.set(objectSqlInfo.getIdColumn() + "=?",
				new Object[] { idValue });
		return this.delete(deleteParam);
	}

	/**
	 * 查询操作，如果QueryParam 中columns没有赋值,则查询表中的所有列，程序将会自动复制列信息到columns属性
	 * 
	 * @param queryParam
	 * @param mapper
	 * @return
	 */
	public <T> List<T> getList(QueryParam queryParam, RowMapper<T> mapper) {
		if (queryParam.getColumns() == null) {
			queryParam.setColumns(this.getColumns(queryParam.getClasses()));
		}
		PartitionTableInfo[] partitionTableInfos = this.parse(
				queryParam.getClasses(), queryParam.getCtxMap());
		return this.getListBySQL(partitionTableInfos[0].getDsKey(), SqlBuilder
				.createListSQL(partitionTableInfos, queryParam.getColumns(),
						queryParam.getWhere(), queryParam.getOrder()),
				queryParam.getParams(), queryParam.getBegin(), queryParam
						.getSize(), mapper);
	}

	/**
	 * select [columns]
	 * 
	 * @param queryParam
	 * @param clazz
	 *            返回结果类型
	 * @return sql 结果集合
	 */
	public <T> List<T> getList(QueryParam queryParam, Class<T> clazz) {
		if (queryParam.getClassCount() == 0) {
			queryParam.addClass(clazz);
		}
		return this.getList(queryParam, this.getRowMapper(clazz));
	}

	/**
	 * count sql 操作
	 * 
	 * @param countParam
	 * @return sql 结果
	 */
	public int count(CountParam countParam) {
		PartitionTableInfo[] partitionTableInfos = this.parse(
				countParam.getClasses(), countParam.getCtxMap());
		return this.countBySQL(
				partitionTableInfos[0].getDsKey(),
				SqlBuilder.createCountSQL(partitionTableInfos,
						countParam.getWhere()), countParam.getParams());
	}

	/**
	 * select sql 对象操作
	 * 
	 * @param queryParam
	 * @param mapper
	 *            参考 {@link RowMapper}
	 * @return
	 */
	public <T> T getObject(QueryParam queryParam, RowMapper<T> mapper) {
		if (queryParam.getColumns() == null) {
			queryParam.setColumns(this.getColumns(queryParam.getClasses()));
		}
		PartitionTableInfo[] partitionTableInfos = this.parse(
				queryParam.getClasses(), queryParam.getCtxMap());
		return this.getObjectBySQL(partitionTableInfos[0].getDsKey(),
				SqlBuilder.createObjectSQL(partitionTableInfos,
						queryParam.getColumns(), queryParam.getWhere(),
						queryParam.getOrder()), queryParam.getParams(), mapper);
	}

	/**
	 * select sql 对象操作
	 * 
	 * @param queryParam
	 * @param clazz
	 *            根据此类型可以获得匹配的mapper
	 * @return
	 */
	public <T> T getObject(QueryParam queryParam, Class<T> clazz) {
		if (queryParam.getClassCount() == 0) {
			queryParam.addClass(clazz);
		}
		return this.getObject(queryParam, this.getRowMapper(clazz));
	}

	/**
	 * select sql 通过id获取对象操作
	 * 
	 * @param ctxMap
	 *            分区key-value的map
	 * @param clazz
	 *            返回的结果对象类型
	 * @param idValue
	 *            对象id
	 * @return 查询结果对象
	 */
	public <T> T getObjectById(Map<String, Object> ctxMap, Class<T> clazz,
			Object idValue) {
		QueryParam queryParam = new QueryParam();
		queryParam.addKeyAdnValueFromMap(ctxMap);
		queryParam.set(this.objectSqlInfoCreater.getObjectSqlInfo(clazz)
				.getIdColumn() + "=?", new Object[] { idValue });
		queryParam.setOrder(null);
		queryParam.setRange(0, 1);
		if (queryParam.getClassCount() == 0) {
			queryParam.addClass(clazz);
		}
		return this.getObject(queryParam, clazz);
	}

	/**
	 * select sql 对象操作
	 * 
	 * @param key
	 *            分区key
	 * @param keyValue
	 *            分区value
	 * @param clazz
	 *            查询的对象类型
	 * @param idValue
	 *            对象id
	 * @return
	 */
	public <T> T getObjectById(String key, Object keyValue, Class<T> clazz,
			Object idValue) {
		Map<String, Object> ctxMap = new HashMap<String, Object>();
		ctxMap.put(key, keyValue);
		return this.getObjectById(ctxMap, clazz, idValue);
	}

	public void setObjectSqlInfoCreater(
			ObjectSqlInfoCreater objectSqlInfoCreater) {
		this.objectSqlInfoCreater = objectSqlInfoCreater;
	}

	/**
	 * 获得真实的操作的数据表信息
	 * 
	 * @param clazz
	 * @param ctxMap
	 * @return
	 */
	public <T> PartitionTableInfo parse(Class<T> clazz,
			Map<String, Object> ctxMap) {
		return this.objectSqlInfoCreater
				.getObjectSqlInfo(clazz)
				.getDbPartitionHelper()
				.parse(this.objectSqlInfoCreater.getObjectSqlInfo(clazz)
						.getTableName(), ctxMap);
	}

	/**
	 * 获得真实的操作的数据表信息
	 * 
	 * @param classes
	 * @param ctxMap
	 * @return
	 */
	public <T> PartitionTableInfo[] parse(Class<?>[] classes,
			Map<String, Object> ctxMap) {
		PartitionTableInfo[] partitionTableInfos = new PartitionTableInfo[classes.length];
		for (int i = 0; i < classes.length; i++) {
			partitionTableInfos[i] = this.parse(classes[i], ctxMap);
		}
		return partitionTableInfos;
	}

	/**
	 * 从classes中获得与数据库对应的字段名称
	 * 
	 * @param classes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> String[][] getColumns(Class<?>[] classes) {
		ObjectSqlInfo<T> objectSqlInfo = null;
		String[][] columns = new String[classes.length][];
		for (int i = 0; i < classes.length; i++) {
			objectSqlInfo = (ObjectSqlInfo<T>) this.objectSqlInfoCreater
					.getObjectSqlInfo(classes[i]);
			columns[i] = objectSqlInfo.getColumns();
		}
		return columns;
	}
}