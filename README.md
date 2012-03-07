# java的轻量级web框架 与分布式数据访问框架
## 框架基础环境：Servlet Spring3 jee5以上版本。 本框架的目的是为了简单的创建并使用web开发以及分布式数据库访问，让开人人员脱离struts2繁琐的配置以及冗余的annotation代码。 你只需要进行简单的配置，即可使用该框架进行web开发.

=======================================================
### web 使用说明:
### 1,导入项目依赖jar文件
### 2,配置web.xml，示例:

<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
        http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
        <!-- spring加载 -->
        <context-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>/WEB-INF/classes/applicationContext.xml</param-value>
        </context-param>
        <!-- spring加载 -->
        <listener>
                <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
        <!-- 字符编码过滤器加载 -->
        <filter>
                <filter-name>encodingFilter</filter-name>
                <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
                <init-param>
                        <param-name>encoding</param-name>
                        <param-value>UTF-8</param-value>
                </init-param>
                <init-param>
                        <param-name>forceEncoding</param-name>
                        <param-value>true</param-value>
                </init-param>
        </filter>
        <!-- 框架filter 0 -->
        <filter>
                <filter-name>HaloWrapperFilter</filter-name>
                <filter-class>halo.web.action.HaloWrapperFilter</filter-class>
        </filter>
        <!-- 框架filter 1 -->
        <filter>
                <filter-name>ActionFilter</filter-name>
                <filter-class>halo.web.action.ActionFilter</filter-class>
        </filter>
        <filter-mapping>
                <filter-name>encodingFilter</filter-name>
                <url-pattern>*.do</url-pattern>
        </filter-mapping>
        <!-- 包装request response，必须放在其他过滤器之前，除了字符编码过滤器 -->
        <filter-mapping>
                <filter-name>HaloWrapperFilter</filter-name>
                <url-pattern>*.do</url-pattern>
        </filter-mapping>
        <!-- action请求过滤器，必须放在所有过滤器之后 -->
        <filter-mapping>
                <filter-name>ActionFilter</filter-name>
                <url-pattern>*.do</url-pattern>
                <!-- 目前测试REQUEST FORWARD 没有问题 -->
                <dispatcher>REQUEST</dispatcher>
                <dispatcher>FORWARD</dispatcher>
        </filter-mapping>
</web-app>

### 3.在spring配置文件中写入:
<!-- 在spring的配置文件中添加 -->
<context:annotation-config />
        <!-- 通过annotation 扫描的根目录 -->
        <context:component-scan base-package="demo.haloweb.dev3g.web">
                <context:include-filter type="annotation" 
                expression="org.springframework.stereotype.Component" />
        </context:component-scan>
        <!-- must config -->
        <bean id="haloUtil" class="halo.util.HaloUtil" />
        <bean class="halo.web.action.ExceptionConfig">
                <property name="exceptionMap">
                        <map>
                            <entry key="java.lang.Exception" value="/web/error.jsp">
                            </entry>
                        </map>
                </property>
        </bean>
        <bean id="webCnf" class="halo.web.util.WebCnf">
                <!-- 设置文件上传的临时目录 -->
                <property name="uploadFileTempPath" value="/Users/fire9/temp/" />
                <!-- 是否需要进行字符编码转换 -->
                <property name="needCharsetEncode" value="true" />
                <!-- 原编码 -->
                <property name="sourceCharset" value="iso-8859-1" />
                <!-- 目标编码 -->
                <property name="targetCharset" value="utf-8" />
                <!-- 强制url进行上传文件检查，不通过配置的url不能接收文件上传 -->
                <property name="mustCheckUpload" value="true" />
                <!-- 接收文件检查的url 格式：/actionname/method:[file size](单位为M) -->
                <property name="fileUploadCheckUriCnfList">
                        <list>
                                <value>/hello/upload:20</value>
                        </list>
                </property>
        </bean>
}
### 4,在demo.haloweb.dev3g.web目录中写一个Action代码

package demo.haloweb.dev3g.web;

import halo.util.FileUtil;
import halo.web.action.HkRequest;
import halo.web.action.HkResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Component;

import demo.haloweb.dev3g.model.User;

/**
 * url运行方式:http://localhost:8080/webapp/hello/{methodname}.do
 * 
 * @author akwei
 */
@Component("/hello")//配置namespace对应
public class HelloAction {

    /**
     * http://localhost:8080/heloweb/hello/say.do <br>
     * 接收http请求参数
     * 
     * @param req
     * @param resp
     * @return
     */
    public String say(HkRequest req, HkResponse resp) {
        String name = "测试中文" + req.getInt("name");//获取int类型参数
        String key = "key" + req.getStringRow("key", "");//获取单行参数
        req.setAttribute("name", name);
        req.setEncodeAttribute("name", name);//setAttribute("enc_name",UrlEncode(name));
        req.setAttribute("key", key);
        req.setSessionValue("name_key", name + key);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        req.setAttribute("list", list);
        req.setAttribute("selectedValue", 5);
        User user = new User();
        req.buildBean(user);//对与user对象属性匹配的参数进行对象赋值，为了简化request.getParameter的过程
        return "/web/hello_say.jsp";//forward的页面
    }

    /**
     * 链接重定向 response.redirect(String);
     * 
     * @param req
     * @param resp
     * @return
     */
    public String say0(HkRequest req, HkResponse resp) {
        return "r:/hello/say.do?name=233&key=hello";//redirect的地址
    }

    /**
     * 文件上传 http://localhost:8080/heloweb/hello/upload.do <br>
     * 
     * @param req
     * @param resp
     * @return
     */
    public String upload(HkRequest req, HkResponse resp) throws Exception {
        if (req.isUploadExceedSize()) {
            resp.sendHtml("图片文件过大");
            return null;
        }
        File file = req.getFile("f");
        if (file == null) {
            resp.sendHtml("没有文件上传");
            return null;
        }
        FileUtil.copyFile(file, "/Users/fire9/mydev/temp", "aa");
        return "/web/upload_success.jsp";
    }

    /**
     * 设置cookie
     * 
     * @param req
     * @param resp
     * @return
     */
    public String setcookie(HkRequest req, HkResponse resp) throws Exception {
        Cookie cookie = new Cookie("cookie", "123");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        resp.addCookie(cookie);
        resp.sendHtml("set cookie ok");
        return null;
    }

    /**
     * 获取cookie
     * 
     * @param req
     * @param resp
     * @return
     */
    public String getcookie(HkRequest req, HkResponse resp) throws Exception {
        Cookie cookie = req.getCookie("cookie");
        String value = cookie.getValue();
        req.setAttribute("value", value);
        return "/web/cookie_get.jsp";
    }
}

### 5,运行tomcat，然后在地址栏输入 http://localhost:8080/webapp/hello/say.do 可以访问say这个方法
到此为基本的mvc运行，里面的Hkrequest HkResponse是HttpServletRequest HttpServletResponse的子类，可以直接使用

