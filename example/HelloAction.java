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