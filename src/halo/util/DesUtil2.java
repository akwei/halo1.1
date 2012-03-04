package halo.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 实验性，暂时不使用
 * 
 * @author akwei
 */
@Deprecated
public class DesUtil2 {

	Key key;

	public DesUtil2(String str) {
		setKey(str);// 生成密匙
	}

	/**
	 * @param strKey
	 */
	public void setKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			// _generator.init(new SecureRandom(strKey.getBytes()));
			this.key = _generator.generateKey();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密String明文输入,String密文输出
	 */
	public String getEncString(String strMing) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = this.getEncCode(byteMing);
			strMi = base64en.encode(byteMi);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @return
	 */
	public String getDesString(String strMi) {
		BASE64Decoder base64De = new BASE64Decoder();
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = base64De.decodeBuffer(strMi);
			byteMing = this.getDesCode(byteMi);
			strMing = new String(byteMing, "UTF8");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private byte[] getEncCode(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	private byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			cipher = null;
		}
		return byteFina;
	}

	public static void main(String args[]) {
		DesUtil2 des = new DesUtil2("my password");
		String str1 = "密文我在这里";
		// DES加密
		String str2 = des.getEncString(str1);
		String deStr = des.getDesString(str2);
		System.out.println("密文:" + str2);
		// DES解密
		System.out.println("明文:" + deStr);
	}
}
