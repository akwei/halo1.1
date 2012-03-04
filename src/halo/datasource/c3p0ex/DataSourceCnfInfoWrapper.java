package halo.datasource.c3p0ex;

/**
 * 对数据库配置信息加密和解密
 * 
 * @author akwei
 */
public interface DataSourceCnfInfoWrapper {

	/**
	 * 获得加密后的密码串
	 * 
	 * @param password
	 *            原始密码
	 * @return
	 */
	String getEncodedPassword(String password);

	/**
	 * 获得解密后的原始密码
	 * 
	 * @param encodePassword
	 * @return
	 */
	String getDecodedPassword(String encodePassword);
}
