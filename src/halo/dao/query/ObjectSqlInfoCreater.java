package halo.dao.query;

import halo.dao.annotation.Table;
import halo.dao.partition.DbPartitionHelper;
import halo.dao.partition.TableCnf;
import halo.util.HaloUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 初始化类与数据表的对应关系配置信息，存储类与数据表的映射对象的数据
 * 
 * @author akwei
 */
public class ObjectSqlInfoCreater implements InitializingBean {

	private final Log log = LogFactory.getLog(ObjectSqlInfoCreater.class);

	/**
	 * class名称为key
	 */
	private final Map<String, ObjectSqlInfo<?>> objectSqlInfoMap = new HashMap<String, ObjectSqlInfo<?>>();

	private List<TableCnf> tableCnfList;

	public void setTableCnfList(List<TableCnf> tableCnfList) throws Exception {
		this.tableCnfList = tableCnfList;
	}

	/**
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> ObjectSqlInfo<T> getObjectSqlInfo(Class<T> clazz) {
		ObjectSqlInfo<T> o = (ObjectSqlInfo<T>) this.objectSqlInfoMap.get(clazz
				.getName());
		if (o == null) {
			o = this.createObjectSqlInfo(clazz);
			if (o != null) {
				objectSqlInfoMap.put(clazz.getName(), o);
			}
		}
		if (o == null) {
			throw new RuntimeException("no ObjectSqlInfo for [ "
					+ clazz.getName() + " ]");
		}
		return o;
	}

	/**
	 * 在配置文件中定义的会提前加载
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		ObjectSqlInfo<?> objectSqlInfo = null;
		if (this.tableCnfList == null) {
			this.tableCnfList = new ArrayList<TableCnf>();
		}
		for (TableCnf cnf : this.tableCnfList) {
			log.info("load tablebean [ " + cnf.getClassName()
					+ " ] with helper [ "
					+ cnf.getDbPartitionHelper().getClass().getName() + " ]");
			objectSqlInfo = new ObjectSqlInfo<Object>(cnf);
			objectSqlInfoMap.put(cnf.getClassName(), objectSqlInfo);
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized <T> ObjectSqlInfo<T> createObjectSqlInfo(Class<T> clazz) {
		ObjectSqlInfo<T> o = (ObjectSqlInfo<T>) this.objectSqlInfoMap.get(clazz
				.getName());
		if (o != null) {
			return o;
		}
		TableCnf tableCnf = this.createTableCnfFromClass(clazz);
		try {
			return new ObjectSqlInfo<T>(tableCnf);
		}
		catch (ClassNotFoundException e) {
			return null;
		}
	}

	private TableCnf createTableCnfFromClass(Class<?> clazz) {
		String className = clazz.getName();
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			throw new RuntimeException(className + " no tableCnf");
		}
		TableCnf tableCnf = new TableCnf();
		tableCnf.setClassName(className);
		tableCnf.setDbPartitionHelper(this
				.getDbPartitionHelperFromTableAnnotation(table));
		log.info("load tablebean [ " + tableCnf.getClassName()
				+ " ] with helper [ "
				+ tableCnf.getDbPartitionHelper().getClass().getName() + " ]");
		return tableCnf;
	}

	private DbPartitionHelper getDbPartitionHelperFromTableAnnotation(
			Table table) {
		DbPartitionHelper dbPartitionHelper = null;
		if (!table.partitionid().equals("")) {
			dbPartitionHelper = (DbPartitionHelper) HaloUtil.getBean(table
					.partitionid());
			if (dbPartitionHelper == null) {
				throw new RuntimeException("can not found spring bean id [ "
						+ table.partitionid() + " ]");
			}
		}
		else {
			try {
				dbPartitionHelper = (DbPartitionHelper) table.partitionClass()
						.getConstructor().newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return dbPartitionHelper;
	}
}