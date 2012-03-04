package halo.dao.query;

/**
 * update sql 操作时需要拼装sql以及赋值的辅助类
 * 
 * @author akwei
 */
public class UpdateParam extends Param {

	public UpdateParam() {
	}

	public UpdateParam(String key, Object value) {
		super(key, value);
	}

	private Class<?> clazz;

	private String[] updateColumns;

	private String where;

	private Object[] params;

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getWhere() {
		return where;
	}

	public Object[] getParams() {
		return params;
	}

	public String[] getUpdateColumns() {
		return updateColumns;
	}

	/**
	 * @param updateColumns
	 *            设置需要更新的列(与数据库字段相同)
	 * @param where
	 *            设置sql的where条件
	 * @param params
	 *            设置条件对应的参数
	 */
	public void set(String[] updateColumns, String where, Object[] params) {
		this.updateColumns = updateColumns;
		this.where = where;
		this.params = params;
	}
}
