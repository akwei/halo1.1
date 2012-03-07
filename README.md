# java的轻量级web框架 与分布式数据访问框架
## 框架基础环境：Servlet Spring3 jee5以上版本。 本框架的目的是为了简单的创建并使用web开发以及分布式数据库访问，让开人人员脱离struts2繁琐的配置以及冗余的annotation代码。 你只需要进行简单的配置，即可使用该框架进行web开发.

=======================================================
### web 使用说明:
### 1,导入项目依赖jar文件
### 2,配置web.xml，示例:example目录中的web.xml

### 3.在spring配置文件中写入:example目录中的spring.xml

### 4,在demo.haloweb.dev3g.web目录中写一个Action代码:example目录中的HelloAction.java


### 5,运行tomcat，然后在地址栏输入 http://localhost:8080/webapp/hello/say.do 可以访问say这个方法
到此为基本的mvc运行，里面的Hkrequest HkResponse是HttpServletRequest HttpServletResponse的子类，可以直接使用

