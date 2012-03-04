package halo.dao.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 参数对象，由于方法传递参数过多，所以封装此对象使用
 * 
 * @author akwei
 */
public abstract class Param {

	private final Map<String, Object> ctxMap = new HashMap<String, Object>(2);

	public Param() {
	}

	public Param(String key, Object value) {
		this.addKeyAndValue(key, value);
	}

	/**
	 * 返回存储的context信息(保存的分区相关信息)
	 * 
	 * @return
	 */
	public Map<String, Object> getCtxMap() {
		return ctxMap;
	}

	/**
	 * 添加分区关键字
	 * 
	 * @param key
	 *            关键字，可以包括别名，必须保证唯一
	 * @param value
	 *            关键字值
	 */
	public void addKeyAndValue(String key, Object value) {
		if (key == null || value == null) {
			return;
		}
		this.ctxMap.put(key, value);
	}

	/**
	 * 获得map中的key-value，如果map中有null值，则不添加相应的key-value
	 * 
	 * @param ctxMap
	 */
	public void addKeyAdnValueFromMap(Map<String, Object> ctxMap) {
		if (ctxMap == null) {
			return;
		}
		Set<Entry<String, Object>> set = ctxMap.entrySet();
		for (Entry<String, Object> e : set) {
			if (e.getValue() != null) {
				this.ctxMap.put(e.getKey(), e.getValue());
			}
		}
	}
}
