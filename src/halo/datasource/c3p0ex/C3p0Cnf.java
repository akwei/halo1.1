package halo.datasource.c3p0ex;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * c3p0连接池组装器，对配置文件进行解析，配置数据源密码加密解密类，完成数据源的创建
 * 
 * @author akwei
 */
public class C3p0Cnf {

	private DataSourceCnfInfoWrapper dataSourceCnfInfoWrapper;

	private String cnfPath;

	public void setDataSourceCnfInfoWrapper(
			DataSourceCnfInfoWrapper dataSourceCnfInfoWrapper) {
		this.dataSourceCnfInfoWrapper = dataSourceCnfInfoWrapper;
	}

	/**
	 * 数据库配置文件路径
	 * 
	 * @param cnfPath
	 */
	public void setCnfPath(String cnfPath) {
		this.cnfPath = cnfPath;
	}

	/**
	 * 使用配置文件，创建c3p0连接池
	 * 
	 * @return
	 */
	public DataSource createDataSource(String dsName) {
		File file = new File(cnfPath);
		DataSourceCnf dataSourceCnf = this.getDataSourceCnf(file, dsName);
		if (dataSourceCnf == null) {
			throw new RuntimeException("no config dsName [ " + dsName + " ]");
		}
		return buildC3p0DataSource(this.dataSourceCnfInfoWrapper, dataSourceCnf);
	}

	protected static ComboPooledDataSource buildC3p0DataSource(
			DataSourceCnfInfoWrapper dataSourceCnfInfoWrapper,
			DataSourceCnf dataSourceCnf) {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		Map<String, String> map = dataSourceCnf.getCnfMap();
		Set<Entry<String, String>> set = map.entrySet();
		for (Entry<String, String> e : set) {
			String fieldName = e.getKey();
			String value = e.getValue();
			setValue(dataSourceCnfInfoWrapper, comboPooledDataSource,
					fieldName, value);
		}
		return comboPooledDataSource;
	}

	protected static void setValue(
			DataSourceCnfInfoWrapper dataSourceCnfInfoWrapper,
			ComboPooledDataSource comboPooledDataSource, String fieldName,
			String value) {
		try {
			if (fieldName.equals("driverClass")) {
				comboPooledDataSource.setDriverClass(value);
			}
			else if (fieldName.equals("jdbcUrl")) {
				comboPooledDataSource.setJdbcUrl(value);
			}
			else if (fieldName.equals("user")) {
				comboPooledDataSource.setUser(value);
			}
			else if (fieldName.equals("password")) {
				if (dataSourceCnfInfoWrapper == null) {
					comboPooledDataSource.setPassword(value);
				}
				else {
					String pwd = dataSourceCnfInfoWrapper
							.getDecodedPassword(value);
					comboPooledDataSource.setPassword(pwd);
				}
			}
			else if (fieldName.equals("maxStatements")) {
				comboPooledDataSource.setMaxStatements(Integer.valueOf(value));
			}
			else if (fieldName.equals("idleConnectionTestPeriod")) {
				comboPooledDataSource.setIdleConnectionTestPeriod(Integer
						.valueOf(value));
			}
			else if (fieldName.equals("maxPoolSize")) {
				comboPooledDataSource.setMaxPoolSize(Integer.valueOf(value));
			}
			else if (fieldName.equals("initialPoolSize")) {
				comboPooledDataSource
						.setInitialPoolSize(Integer.valueOf(value));
			}
			else if (fieldName.equals("minPoolSize")) {
				comboPooledDataSource.setMinPoolSize(Integer.valueOf(value));
			}
		}
		catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建配置文件map
	 * 
	 * @param file
	 *            配置文件
	 * @return
	 */
	protected static Map<String, DataSourceCnf> createDataSourceCnfMapFromFile(
			File file) {
		try {
			List<String> list = readFile(file);
			return parse(list);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected static Map<String, DataSourceCnf> parse(final List<String> list) {
		List<CnfPos> olist = createCnfPosList(list);
		Map<String, DataSourceCnf> map = new HashMap<String, DataSourceCnf>();
		DataSourceCnf dataSourceCnf = null;
		for (CnfPos o : olist) {
			dataSourceCnf = createDataSourceCnf(o, list);
			map.put(dataSourceCnf.getDsName(), dataSourceCnf);
		}
		return map;
	}

	protected static DataSourceCnf createDataSourceCnf(final CnfPos cnfPos,
			final List<String> list) {
		DataSourceCnf o = new DataSourceCnf();
		String dsName = list.subList(cnfPos.getBegin(), cnfPos.getBegin() + 1)
				.get(0).replaceAll("=\\{", "");
		o.setDsName(dsName);
		List<String> olist = list.subList(cnfPos.getBegin() + 1,
				cnfPos.getEnd());
		for (String s : olist) {
			String[] k_v = s.split(":", 2);
			o.setKeyAndValue(k_v[0], k_v[1]);
		}
		return o;
	}

	protected static List<CnfPos> createCnfPosList(final List<String> list) {
		int cnfBegin = -1;
		int cnfEnd = -1;
		List<CnfPos> olist = new ArrayList<CnfPos>();
		String s = null;
		for (int i = 0; i < list.size(); i++) {
			s = list.get(i);
			if (s.startsWith("#")) {
				continue;
			}
			if (s.endsWith("={")) {// 寻找开始位置
				cnfBegin = i;
			}
			if (s.endsWith("}")) {// 寻找结束位置
				cnfEnd = i;
				CnfPos cnfPos = new CnfPos();
				cnfPos.setBegin(cnfBegin);
				cnfPos.setEnd(cnfEnd);
				olist.add(cnfPos);
			}
		}
		return olist;
	}

	protected static List<String> readFile(File file) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "utf-8"));
			List<String> list = new ArrayList<String>();
			String s = null;
			while ((s = reader.readLine()) != null) {
				list.add(s.trim());
			}
			return list;
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * 获取指定与dsName匹配的配置信息
	 * 
	 * @param file
	 *            配置文件
	 * @param dsName
	 * @return 如果不存在就返回null
	 */
	private DataSourceCnf getDataSourceCnf(File file, String dsName) {
		Map<String, DataSourceCnf> map = createDataSourceCnfMapFromFile(file);
		return map.get(dsName);
	}
}