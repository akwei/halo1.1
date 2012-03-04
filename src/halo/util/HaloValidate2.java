package halo.util;

public class HaloValidate2 {

	/**
	 * 验证数据，如果数据为空或者长度超出范围，就是验证失败
	 * 
	 * @param value
	 *            需要验证的数据
	 * @param maxLen
	 *            最大长度
	 * @return true:验证通过,false:验证失败 2010-6-8
	 */
	public static boolean validateEmptyAndLength(String value, int maxLen) {
		if (DataUtil.isEmpty(value)) {
			return false;
		}
		return validateLength(value, maxLen);
	}

	/**
	 * 如果为空或则不在范围之内，验证失败
	 * 
	 * @param value
	 * @param minLen
	 * @param maxLen
	 * @return
	 */
	public static boolean validateEmptyAndRange(String value, int minLen,
			int maxLen) {
		if (DataUtil.isEmpty(value)) {
			return false;
		}
		if (value.length() >= minLen && value.length() <= maxLen) {
			return true;
		}
		return false;
	}

	/**
	 * 如果为空或在范围之内，验证通过
	 * 
	 * @param value
	 * @param maxLen
	 * @return
	 */
	public static boolean validateLength(String value, int maxLen) {
		if (DataUtil.isEmpty(value)) {
			return true;
		}
		if (value.length() > maxLen) {
			return false;
		}
		return true;
	}

	/**
	 * 如果为空或在范围之内，验证通过。
	 * 
	 * @param value
	 * @param minLen
	 * @param maxLen
	 * @return
	 */
	public static boolean validateRange(String value, int minLen, int maxLen) {
		if (DataUtil.isEmpty(value)) {
			return true;
		}
		if (value.length() >= minLen && value.length() <= maxLen) {
			return true;
		}
		return false;
	}
}