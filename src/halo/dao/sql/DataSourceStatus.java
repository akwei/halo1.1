package halo.dao.sql;

/**
 * 保存当前使用的数据库key
 * 
 * @author akwei
 */
public class DataSourceStatus {

	private static final ThreadLocal<String> currentDsKey = new ThreadLocal<String>();

	public static void setCurrentDsKey(String dsKey) {
		currentDsKey.set(dsKey);
	}

	public static void removeCurrentDsKey() {
		currentDsKey.remove();
	}

	public static String getCurrentDsKey() {
		return currentDsKey.get();
	}
}