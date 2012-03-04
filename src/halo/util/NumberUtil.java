package halo.util;

import java.text.DecimalFormat;
import java.util.Random;

public class NumberUtil {

	public static long getLong(Object obj) {
		if (obj instanceof String) {
			return Long.parseLong((String) obj);
		}
		Number n = getNumber(obj);
		if (n == null) {
			return 0L;
		}
		return n.longValue();
	}

	public static int getInt(Object obj) {
		if (obj instanceof String) {
			return Integer.parseInt((String) obj);
		}
		Number n = getNumber(obj);
		if (n == null) {
			return 0;
		}
		return n.intValue();
	}

	public static Number getNumber(Object obj) {
		if (obj == null) {
			return null;
		}
		return (Number) obj;
	}

	public static String fmtDouble(double number, String pattern) {
		DecimalFormat fmt = new DecimalFormat(pattern);
		return fmt.format(number);
	}

	public static int getRandomNumber(int maxNumber) {
		Random r = new Random();
		return r.nextInt(maxNumber);
	}
}
