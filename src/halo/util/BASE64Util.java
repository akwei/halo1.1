package halo.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class BASE64Util {

	private BASE64Util() {//
	}

	private static BASE64Encoder encoder = new BASE64Encoder();

	private static BASE64Decoder decoder = new BASE64Decoder();

	public static byte[] decodeForBytes(String s) {
		try {
			return decoder.decodeBuffer(s);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String decode(String s) {
		if (s == null) {
			return null;
		}
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encode(byte[] by) {
		return encoder.encode(by);
	}

	public static String encode(String str) {
		try {
			return encoder.encode(str.getBytes("utf-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(BASE64Util.encode("123"));
			System.out.println(BASE64Util.decode("MTIz"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
