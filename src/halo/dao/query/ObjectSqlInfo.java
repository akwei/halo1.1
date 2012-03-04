package halo.dao.query;

import halo.dao.annotation.Column;
import halo.dao.annotation.Id;
import halo.dao.annotation.Table;
import halo.dao.partition.DbPartitionHelper;
import halo.dao.partition.TableCnf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类-表映射数据。存储class 对应的 sql相关信息
 * 
 * @author akwei
 */
public class ObjectSqlInfo<T> {

	/**
	 * 插入，更新，删除的映射
	 */
	private SqlUpdateMapper<T> sqlUpdateMapper;

	/**
	 * 数据分区分析器
	 */
	private DbPartitionHelper dbPartitionHelper;

	/**
	 * 类信息
	 */
	private Class<T> clazz;

	/**
	 * sql所有字段的数组
	 */
	private String[] columns;

	/**
	 * sql进行update时，需要更新的字段数组
	 */
	private String[] columnsForUpdate;

	/**
	 * 逻辑表名称
	 */
	private String tableName;

	/**
	 * id字段属性
	 */
	private Field idField;

	/**
	 * id列名
	 */
	private String idColumn;

	/**
	 * 除id字段的其他字段信息集合
	 */
	private final List<Field> fieldIgnoreIdList = new ArrayList<Field>();

	/**
	 * 所有字段信息集合
	 */
	private final List<Field> allFieldList = new ArrayList<Field>();

	/**
	 * 属性名称对应的列名称(列名称小写后成为sql字段名称)filed:column
	 */
	private final Map<String, String> fieldColumnMap = new HashMap<String, String>();

	/**
	 * @param tableCnf
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ObjectSqlInfo(TableCnf tableCnf) throws ClassNotFoundException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		this.clazz = (Class<T>) classLoader.loadClass(tableCnf.getClassName());
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			throw new RuntimeException("tableName not set [ " + clazz.getName()
					+ " ]");
		}
		this.tableName = table.name();
		Field[] fs = clazz.getDeclaredFields();
		for (Field f : fs) {
			this.analyze(f);
		}
		this.buildAllColumns();
		// this.buildMapper();
		this.buildSqlUpdateMapper();
		this.dbPartitionHelper = tableCnf.getDbPartitionHelper();
	}

	private void analyze(Field field) {
		// 分析与数据库对应的字段
		Column column = field.getAnnotation(Column.class);
		if (column != null) {
			field.setAccessible(true);
			fieldIgnoreIdList.add(field);
			allFieldList.add(field);
			// 设置属性与sql字段的名称对应关系，默认是属性的名称与字段名相等，除非自定义Column中的name
			if (column.value().equals("")) {
				fieldColumnMap.put(field.getName(), field.getName()
						.toLowerCase());
			}
			else {
				fieldColumnMap.put(field.getName(), column.value());
			}
		}
		else {
			// 定义的sql中的 id 字段,id字段也属于Column
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				field.setAccessible(true);
				allFieldList.add(field);
				this.setIdField(field);
				if (id.value().equals("")) {
					this.setIdColumn(field.getName().toLowerCase());
					fieldColumnMap.put(field.getName(), field.getName()
							.toLowerCase());
				}
				else {
					this.setIdColumn(id.value());
					fieldColumnMap.put(field.getName(), id.value());
				}
			}
		}
	}

	public String getColumn(String fieldName) {
		return fieldColumnMap.get(fieldName);
	}

	private void buildSqlUpdateMapper() {
		SqlUpdateMapperCreater sqlUpdateMapperCreater = new SqlUpdateMapperCreater(
				Thread.currentThread().getContextClassLoader());
		Class<SqlUpdateMapper<T>> clazz = sqlUpdateMapperCreater
				.createSqlUpdateMapperClass(this);
		try {
			this.sqlUpdateMapper = clazz.getConstructor().newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建update需要的列
	 */
	private void buildAllColumns() {
		columns = new String[this.allFieldList.size()];
		int i = 0;
		for (Field f : this.allFieldList) {
			columns[i] = this.getColumn(f.getName());
			i++;
		}
		i = 0;
		columnsForUpdate = new String[this.fieldIgnoreIdList.size()];
		for (Field f : this.fieldIgnoreIdList) {
			columnsForUpdate[i] = this.getColumn(f.getName());
			i++;
		}
	}

	/**
	 * 获得id字段
	 * 
	 * @return
	 */
	public Field getIdField() {
		return idField;
	}

	/**
	 * 设置@Id字段
	 * 
	 * @param idField
	 */
	public void setIdField(Field idField) {
		this.idField = idField;
	}

	/**
	 * 获得数据库对应的id字段
	 * 
	 * @return
	 */
	public String getIdColumn() {
		return idColumn;
	}

	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}

	/**
	 * 获得除去id的所有字段
	 * 
	 * @return
	 */
	public List<Field> getFieldIgnoreIdList() {
		return fieldIgnoreIdList;
	}

	/**
	 * 获得逻辑表名
	 * 
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表别名
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获得所有字段
	 * 
	 * @return
	 */
	public List<Field> getAllfieldList() {
		return allFieldList;
	}

	/**
	 * 获得进行解析的类
	 * 
	 * @return
	 */
	public Class<T> getClazz() {
		return clazz;
	}

	/**
	 * 获得SqlUpdateMapper对象
	 * 
	 * @return
	 */
	public SqlUpdateMapper<T> getSqlUpdateMapper() {
		return sqlUpdateMapper;
	}

	/**
	 * 获得sql所有字段
	 * 
	 * @return
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * 获得sqlforupdate的所有字段
	 * 
	 * @return
	 */
	public String[] getColumnsForUpdate() {
		return columnsForUpdate;
	}

	/**
	 * 获得数据分区分析器
	 * 
	 * @return
	 */
	public DbPartitionHelper getDbPartitionHelper() {
		return dbPartitionHelper;
	}
}