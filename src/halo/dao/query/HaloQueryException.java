package halo.dao.query;

public class HaloQueryException extends RuntimeException {

	private static final long serialVersionUID = -8333257204418273331L;

	public HaloQueryException() {
	}

	public HaloQueryException(String arg0) {
		super(arg0);
	}

	public HaloQueryException(Throwable arg0) {
		super(arg0);
	}

	public HaloQueryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
