package halo.dao.query;

/**
 * delete时的参数对象 deleteById时,where、params参数无效
 * 
 * @author akwei
 */
public class DeleteParam extends Param {

	public DeleteParam() {
	}

	public DeleteParam(String key, Object value) {
		super(key, value);
	}

	private Class<?> clazz;

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

	public void set(String where, Object[] params) {
		this.where = where;
		this.params = params;
	}
}
