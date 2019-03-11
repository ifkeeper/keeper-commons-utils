package com.mingrn.itumate.commons.utils.encrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * DESede 加密
 * <br>
 * DESede是由DES对称加密算法改进后的一种对称加密算法。使用 168 位的密钥对资料进行三次加密的一种机制;
 * 它通常(但非始终)提供极其强大的安全性。如果三个 56 位的子元素都相同,则三重 DES 向后兼容 DES。
 */
public final class TripleDESUtil {

	private TripleDESUtil() {
	}

	/**
	 * 定义加密算法,可用 DES,DESede,Blowfish
	 */
	private static final String ENCRYPT_ALGORITHM = "DESede";

	/**
	 * 加密算法
	 *
	 * @param password 为加密密钥
	 * @param data 为被加密的数据
	 */
	public static byte[] encryptMode(String password, byte[] data) {
		//生成密钥
		SecretKey desKey = new SecretKeySpec(password.getBytes(), ENCRYPT_ALGORITHM);
		//加密
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密算法
	 *
	 * @param password 为加密密钥
	 * @param data 为被加密的数据
	 */
	public static byte[] decryptMode(String password, byte[] data) throws Exception {
		//生成密钥
		SecretKey desKey = new SecretKeySpec(password.getBytes(), ENCRYPT_ALGORITHM);
		//解密
		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, desKey);
		return cipher.doFinal(data);
	}

	/**
	 * 转换成十六进制字符串
	 */
	public static String byte2hex(byte[] b) {
		String tmp;
		StringBuilder builder = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			tmp = (Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				builder.append("0").append(tmp);
			} else {
				builder.append(tmp);
			}
			if (n < b.length - 1) {
				builder.append(":");
			}
		}
		return builder.toString().toUpperCase();
	}

}