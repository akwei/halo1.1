package halo.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DesUtilBase64 {

	private Cipher encryptCipher = null;

	private Cipher decryptCipher = null;

	private BASE64Encoder encoder = new BASE64Encoder();

	private BASE64Decoder decoder = new BASE64Decoder();

	private String encodeWithBase64(byte[] arrB) throws Exception {
		return encoder.encode(arrB);
	}

	private byte[] decodeWithBase64(String strIn) throws Exception {
		return decoder.decodeBuffer(strIn);
	}

	public DesUtilBase64(String strKey) {
		java.security.Security
				.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key;
		try {
			key = getKey(strKey);
			encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密字节数组
	 * 
	 * @param arrB
	 *            需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	private byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public String encrypt(String strIn) {
		try {
			return encodeWithBase64(encrypt(strIn.getBytes()));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密字节数组
	 * 
	 * @param arrB
	 *            需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	private byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public String decrypt(String strIn) {
		try {
			return new String(decrypt(decodeWithBase64(strIn)));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
	 * 
	 * @param arrBTmp
	 *            构成该字符串的字节数组
	 * @return 生成的密钥
	 * @throws java.lang.Exception
	 */
	private Key getKey(String keyStr) {
		byte[] arrBTmp = keyStr.getBytes();
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];
		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		// 生成密钥
		Key key = new SecretKeySpec(arrB, "DES");
		return key;
	}

	public static void main(String[] args) {
		String text = "text to encrypt";
		// String text = "加密数据测试";
		String key = "my password";
		DesUtilBase64 desUtilBase64_0 = new DesUtilBase64(key);
		DesUtilBase64 desUtilBase64_1 = new DesUtilBase64(key);
		String enc_text = desUtilBase64_0.encrypt(text);
		String dec_text = desUtilBase64_1.decrypt(enc_text);
		System.out.println("加密前的字符：" + text);
		System.out.println("加密后的字符：" + enc_text);
		System.out.println("解密后的字符：" + dec_text);
	}
}