package halo.dao.query;

public class SqlBuilder {

	/**
	 * 创建 select count sql片段<br/>
	 * 例如：参数为userid,则返回select count(userid)。参数为null，返回select count(*)
	 * 
	 * @param columns
	 *            需要count 的字段，可以为null
	 * @return
	 */
	public static String buildSelectCount(String columns) {
		StringBuilder sb = new StringBuilder("select count(");
		if (columns != null) {
			sb.append(columns);
		}
		else {
			sb.append("*");
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 产生需要查询的列sql片段。<br/>
	 * 例如：查询user表的userid,nick，就会自动添加别名返回 select user.userid,user.nick
	 * 
	 * @param partitionTableInfos
	 * @param columns
	 * @return
	 */
	public static String buildSelectColumns(
			PartitionTableInfo[] partitionTableInfos, String[][] columns) {
		StringBuilder sb = new StringBuilder("select ");
		int k1 = 0;
		int k2 = 0;
		for (int i = 0; i < columns.length; i++) {
			k1 = i;
			k2 = columns[i].length - 1;
		}
		for (int i = 0; i < partitionTableInfos.length; i++) {
			for (int k = 0; k < columns[i].length; k++) {
				sb.append(partitionTableInfos[i].getAliasName()).append(".");
				sb.append(columns[i][k]);
				if (i == k1 && k == k2) {
				}
				else {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 产生insert sql
	 * 
	 * @param partitionTableInfo表信息
	 * @param columns
	 *            需要插入的列
	 * @return
	 */
	public static String buildInsert(PartitionTableInfo partitionTableInfo,
			String[] columns) {
		StringBuilder sb = new StringBuilder("insert into ");
		sb.append(partitionTableInfo.getTableName());
		sb.append("(");
		for (int i = 0; i < columns.length; i++) {
			sb.append(columns[i]);
			if (i != columns.length - 1) {
				sb.append(",");
			}
		}
		sb.append(") values(");
		for (int i = 0; i < columns.length; i++) {
			sb.append("?");
			if (i != columns.length - 1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 产生 update sql 片段<br/>
	 * 例如：user表更新update user set userid=?,nick=?
	 * 
	 * @param partitionTableInfo
	 *            表信息
	 * @param columns
	 *            需要更新的列
	 * @return
	 */
	public static String buildUpdate(PartitionTableInfo partitionTableInfo,
			String[] columns) {
		StringBuilder sb = new StringBuilder("update ");
		sb.append(partitionTableInfo.getTableName());
		sb.append(" set ");
		for (int i = 0; i < columns.length; i++) {
			sb.append(columns[i]).append("=?");
			if (i != columns.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 产生delete sql片段<br/>
	 * 例如：delete from user
	 * 
	 * @param partitionTableInfo
	 *            表信息
	 * @return
	 */
	public static String buildDelete(PartitionTableInfo partitionTableInfo) {
		return "delete from " + partitionTableInfo.getTableName();
	}

	/**
	 * 产生 from sql 片段(主要提供给查询使用)。表别名可与表名相同<br/>
	 * 例如from user user。from user0 user,member1 member
	 * 
	 * @param partitionTableInfos
	 * @return
	 */
	public static String buildFrom(PartitionTableInfo[] partitionTableInfos) {
		StringBuilder sb = new StringBuilder(" from ");
		for (int i = 0; i < partitionTableInfos.length; i++) {
			sb.append(partitionTableInfos[i].getTableName());
			sb.append(" ");
			sb.append(partitionTableInfos[i].getAliasName());
			if (i < partitionTableInfos.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 产生count sql<br/>
	 * 例如：select count(*) from user。select count(*) from user user,member member
	 * where user.userid=member.userid
	 * 
	 * @param partitionTableInfos
	 *            表信息
	 * @param where
	 *            查询条件
	 * @return sql 语句
	 */
	public static String createCountSQL(
			PartitionTableInfo[] partitionTableInfos, String where) {
		StringBuilder sb = new StringBuilder(buildSelectCount("*"));
		sb.append(buildFrom(partitionTableInfos));
		if (where != null) {
			sb.append(" where ");
			sb.append(where);
		}
		return sb.toString();
	}

	/**
	 * 产生insert sql
	 * 
	 * @param partitionTableInfo
	 *            表信息
	 * @param columns
	 *            要插入的表列
	 * @return
	 */
	public static String createInsertSQL(PartitionTableInfo partitionTableInfo,
			String[] columns) {
		StringBuilder sb = new StringBuilder(buildInsert(partitionTableInfo,
				columns));
		return sb.toString();
	}

	/**
	 * 产生查询集合的sql
	 * 
	 * @param partitionTableInfos
	 *            表信息
	 * @param columns
	 *            查询列
	 * @param where
	 *            查询条件，没有可为null
	 * @param order
	 *            排序条件，没有可为null
	 * @return sql语句
	 */
	public static String createListSQL(
			PartitionTableInfo[] partitionTableInfos, String[][] columns,
			String where, String order) {
		StringBuilder sb = new StringBuilder(buildSelectColumns(
				partitionTableInfos, columns));
		sb.append(buildFrom(partitionTableInfos));
		if (where != null) {
			sb.append(" where ");
			sb.append(where);
		}
		if (order != null) {
			sb.append(" order by ");
			sb.append(order);
		}
		return sb.toString();
	}

	/**
	 * 产生查询单条记录的sql语句
	 * 
	 * @param partitionTableInfos
	 *            表信息
	 * @param columns
	 *            查询的列
	 * @param where
	 *            查询条件
	 * @param order
	 *            排序条件
	 * @return 完整的sql语句
	 */
	public static String createObjectSQL(
			PartitionTableInfo[] partitionTableInfos, String[][] columns,
			String where, String order) {
		StringBuilder sb = new StringBuilder(buildSelectColumns(
				partitionTableInfos, columns));
		sb.append(buildFrom(partitionTableInfos));
		if (where != null) {
			sb.append(" where ");
			sb.append(where);
		}
		if (order != null) {
			sb.append(" order by ");
			sb.append(order);
		}
		return sb.toString();
	}

	/**
	 * 产生 update sql
	 * 
	 * @param partitionTableInfo
	 *            表信息
	 * @param columns
	 *            要更新的列
	 * @param where
	 *            条件 sql
	 * @return
	 */
	public static String createUpdateSQL(PartitionTableInfo partitionTableInfo,
			String[] columns, String where) {
		StringBuilder sb = new StringBuilder(buildUpdate(partitionTableInfo,
				columns));
		if (where != null) {
			sb.append(" where ");
			sb.append(where);
		}
		return sb.toString();
	}

	/**
	 * 产生delete sql
	 * 
	 * @param partitionTableInfo
	 *            表信息
	 * @param where
	 *            条件sql
	 * @return
	 */
	public static String createDeleteSQL(PartitionTableInfo partitionTableInfo,
			String where) {
		StringBuilder sb = new StringBuilder(buildDelete(partitionTableInfo));
		if (where != null) {
			sb.append(" where ");
			sb.append(where);
		}
		return sb.toString();
	}
}
