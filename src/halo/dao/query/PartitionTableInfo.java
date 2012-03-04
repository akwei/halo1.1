package halo.dao.query;

/**
 * 数据库表的基本信息，包括数据库真是名称与表真是名称
 * 
 * @author akwei
 */
public class PartitionTableInfo {

	/**
	 * 数据源key
	 */
	private String dsKey;

	/**
	 * 表真是名称
	 */
	private String tableName;

	/**
	 * 表的别名
	 */
	private String aliasName;

	public PartitionTableInfo() {
	}

	public PartitionTableInfo(String dsKey, String tableName) {
		this.dsKey = dsKey;
		this.tableName = tableName;
	}

	public String getDsKey() {
		return dsKey;
	}

	/**
	 * 设置数据库真实key
	 * 
	 * @param dsKey
	 */
	public void setDsKey(String dsKey) {
		this.dsKey = dsKey;
	}

	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表真实名称
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 设置表别名
	 * 
	 * @param aliasName
	 */
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getAliasName() {
		return aliasName;
	}
}
