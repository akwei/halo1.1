package halo.dao.sql;

import java.sql.Connection;

/**
 * Connection代理,不产生实际的connection资源
 * 
 * @author yuanwei
 */
public interface ConnectionProxy extends Connection {

	/**
	 * 获得当前使用的Connection
	 * 
	 * @return
	 * @see Connection
	 */
	Connection getCurrentConnection();
}