package halo.dao.partition;

import halo.dao.query.PartitionTableInfo;
import halo.dao.sql.HkDataSourceWrapper;

import java.util.Map;

public class DbPartitionHelperDef extends DbPartitionHelper {

	@Override
	public PartitionTableInfo parse(String name, Map<String, Object> ctxMap) {
		PartitionTableInfo partitionTableInfo = new PartitionTableInfo();
		partitionTableInfo.setDsKey(HkDataSourceWrapper.DEFAULT_DBKEY);
		partitionTableInfo.setTableName(name);
		partitionTableInfo.setAliasName(name);
		return partitionTableInfo;
	}
}