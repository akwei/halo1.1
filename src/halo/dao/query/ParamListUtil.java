package halo.dao.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParamListUtil {

	private List<Object> list = new ArrayList<Object>();

	public void addInt(int value) {
		this.list.add(value);
	}

	public void addLong(long value) {
		this.list.add(value);
	}

	public void addString(String value) {
		this.list.add(value);
	}

	public void addByte(byte value) {
		this.list.add(value);
	}

	public void addShort(short value) {
		this.list.add(value);
	}

	public void addDate(Date value) {
		this.list.add(value);
	}

	public void addChar(char value) {
		this.list.add(value);
	}

	public void addFloat(float value) {
		this.list.add(value);
	}

	public void addDouble(double value) {
		this.list.add(value);
	}

	public Object[] toObjects() {
		return this.list.toArray(new Object[this.list.size()]);
	}

	public static Object toObject(int value) {
		return value;
	}

	public static Object toObject(long value) {
		return Long.valueOf(value);
	}

	public static Object toObject(short value) {
		return value;
	}

	public static Object toObject(byte value) {
		return value;
	}

	public static Object toObject(boolean value) {
		return value;
	}

	public static Object toObject(float value) {
		return value;
	}

	public static Object toObject(double value) {
		return value;
	}

	public static Object toObject(String value) {
		return value;
	}

	public static Object toObject(Date value) {
		return value;
	}
}