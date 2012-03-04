package halo.util;

import java.security.MessageDigest;

/**
 * MD5的算法在RFC1321 中定义<br>
 * 在RFC 1321中，给出了Test suite用来检验你的实现是否正确：<br>
 * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e <br>
 * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661 <br>
 * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72 <br>
 * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0 <br>
 * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
 * 
 * @author akwei
 */
public class MD5Util {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private static final String MD5 = "MD5";

	private static final String CHARSET_UTF_8 = "utf-8";

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String md5Encode32(String v) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(MD5);
			return byteArrayToHexString(md.digest(v.getBytes(CHARSET_UTF_8)));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String md5Encode16(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes("utf-8"));
			byte b[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().substring(8, 24);
			// System.out.println("result: " + buf.toString());// 32位的加密
			// System.out.println("result: " + buf.toString().substring(8,
			// 24));// 16位的加密
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		P.println(MD5Util.md5Encode32("123456"));
	}
}