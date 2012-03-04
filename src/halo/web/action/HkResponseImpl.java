package halo.web.action;

import halo.web.util.ServletUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HkResponseImpl extends HttpServletResponseWrapper implements
		HkResponse {

	public HkResponseImpl(HttpServletResponse response) {
		super(response);
	}

	private HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse) this.getResponse();
	}

	@Override
	public void sendHtml(Object value) {
		ServletUtil.sendHtml(this.getHttpServletResponse(), value);
	}

	@Override
	public void sendXML(String value) {
		ServletUtil.sendXml(this.getHttpServletResponse(), value);
	}

	@Override
	public void sendJson(String json) {
		ServletUtil.sendJson(this.getHttpServletResponse(), json);
	}
}