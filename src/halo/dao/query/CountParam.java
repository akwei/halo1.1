package halo.dao.query;

import java.util.ArrayList;
import java.util.List;

public class CountParam extends Param {

	private final List<Class<?>> classList = new ArrayList<Class<?>>(2);

	public CountParam() {
		super();
	}

	public CountParam(String key, Object value) {
		super(key, value);
	}

	/**
	 * 添加需要查询的类
	 * 
	 * @param <T>
	 * @param clazz
	 *            需要查询的类
	 */
	public <T> void addClass(Class<T> clazz) {
		if (!this.classList.contains(clazz)) {
			this.classList.add(clazz);
		}
	}

	/**
	 * 获取要查询的类<br/>
	 * 根据类信息可获取表信息
	 * 
	 * @return
	 */
	public Class<?>[] getClasses() {
		return this.classList.toArray(new Class<?>[this.classList.size()]);
	}

	private String where;

	private Object[] params;

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
