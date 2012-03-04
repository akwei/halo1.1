package halo.dao.partition;

/**
 * 设定daosupport标识，用来表明数据库类型
 * 
 * @author akwei
 */
public interface DaoIdentifier {

	/**
	 * db2数据库前缀标识
	 */
	String IDENTIFIER_DB2 = "db2";

	/**
	 * mysql数据库前缀标识
	 */
	String IDENTIFIER_MYSQL = "mysql";

	/**
	 * oracle数据库前缀标识
	 */
	String IDENTIFIER_ORACLE = "oracle";

	/**
	 * 返回数据库类型标识
	 * 
	 * @return
	 */
	String getIdentifier();
}
