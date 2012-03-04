package halo.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 类-表映射数据。存储class 对应的 sql相关信息
 * 
 * @author akwei
 */
public class ClassInfoFactory {

	private static final Map<String, ClassInfo<?>> map = new HashMap<String, ClassInfo<?>>();

	@SuppressWarnings("unchecked")
	public static synchronized <T> ClassInfo<T> getClassInfo(Class<T> clazz) {
		ClassInfo<T> o = (ClassInfo<T>) map.get(clazz.getName());
		if (o == null) {
			o = createClassInfo(clazz);
			map.put(clazz.getName(), o);
		}
		return o;
	}

	private static <T> ClassInfo<T> createClassInfo(Class<T> clazz) {
		return new ClassInfo<T>(clazz);
	}
}