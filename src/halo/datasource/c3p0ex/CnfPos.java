package halo.datasource.c3p0ex;

public class CnfPos {

	private int begin;

	private int end;

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

	@Override
	public String toString() {
		return begin + "|" + end;
	}
}
