package halo.web.action;

import javax.servlet.http.HttpServletResponse;

public interface HkResponse extends HttpServletResponse {

	void sendXML(String value);

	void sendHtml(Object value);

	void sendJson(String json);
}