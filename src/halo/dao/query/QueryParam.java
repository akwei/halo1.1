package halo.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件参数的对象表示方式，完全是为了程序易读<br/>
 * 集合查询时，所有参数都要用到<br/>
 * 查询单个对象时，不需要用到 begin ,size<br/>
 * 
 * @author akwei
 */
public class QueryParam extends Param {

    public QueryParam() {
    }

    public QueryParam(String key, Object value) {
        super(key, value);
    }

    private final List<Class<?>> classList = new ArrayList<Class<?>>(2);

    private String[][] columns;

    private String order;

    private int begin;

    private int size;

    private String where;

    private Object[] params;

    public String getWhere() {
        return where;
    }

    public Object[] getParams() {
        return params;
    }

    /**
     * 添加需要查询的类。添加类的顺序会影响到设置现实字段的顺序。添加类的顺序一定要与columns的顺序一致<br/>
     * 在单表查询情况下，如果查询的表与返回值类型相同，可不设置此参数
     * 
     * @param <T>
     * @param clazz
     *            需要查询的类
     */
    public <T> void addClass(Class<T> clazz) {
        if (!this.classList.contains(clazz)) {
            this.classList.add(clazz);
        }
    }

    /**
     * 获得需要查询的列，是与addClass表一致的顺序
     * 
     * @return
     */
    public String[][] getColumns() {
        return columns;
    }

    /**
     * 设置数据库需要显示的字段。(注意：是数据库字段，不是代码表现的属性)。<br/>
     * 添加的顺序一定要与addClass的顺序一致<br/>
     * 例如 addClass(User.class);addClass(Member.class); setColumns的参数为：<br/>
     * new String[][]{<br/>
     * {userid,nick,gender}(user表),<br/>
     * {memberid,membername,birthday}(member表)<br/>
     */
    public void setColumns(String[][] columns) {
        this.columns = columns;
    }

    /**
     * 获得order by的排序sql片段
     * 
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     * 设置sql中 order部分
     * 
     * @param order
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 获得获取数据集合的开始位置
     * 
     * @return
     */
    public int getBegin() {
        return begin;
    }

    /**
     * 获取数据集合的数量<br/>
     * <=0时，获取所有数据;>0时获取指定数量数据
     * 
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * 获取要查询的类<br/>
     * 根据类信息可获取表信息
     * 
     * @return
     */
    public Class<?>[] getClasses() {
        return this.classList.toArray(new Class<?>[this.classList.size()]);
    }

    public int getClassCount() {
        return this.classList.size();
    }

    public void set(String where, Object[] params) {
        this.where = where;
        this.params = params;
    }

    /**
     * 设置查询数据范围
     * 
     * @param begin
     *            设置获取数据开始的位置
     * @param size
     *            设置获取数据的数量。<=0时，获取所有数据;>0时获取指定数量数据
     */
    public void setRange(int begin, int size) {
        this.begin = begin;
        this.size = size;
    }
}