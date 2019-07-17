package com.cares.sh.httpClient.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Md5Util {
	
	/**
	 * @Title: signRequest  
	 * @Description: MD5��������
	 * @param params ����
	 * @param secret ��Կ
	 * @param signMethod
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String signRequest(Map<String, String> params, String secret, String signMethod)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		/**
		 * 1.����������ASCII���������
		 */
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		/**
		 * 2.�����в���ƴ����һ��
		 */
		StringBuilder sb = new StringBuilder();
		sb.append(secret);
		for (String key : keys) {
			String value = params.get("key");
			if (!"".equals(value) || value != null) {
				sb.append(key).append(value);
			}
		}
		sb.append(secret);
		/**
		 * 3.MD5����
		 */
		byte[] bytes = encryptMD5(sb.toString());
		/**
		 * 4.�Ѷ�����ת��Ϊ��д��ʮ������
		 */
		return byte2hex(bytes);
	}

	/**  
	 * @Title: encryptMD5  
	 * @Description: MD5����
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException 
	 */
	public static byte[] encryptMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("md5");
		digest.update(str.getBytes("utf-8"));
		return digest.digest();
	}

	/**  
	 * @Title: byte2hex  
	 * @Description: ������ת����ʮ�����Ƶ��ַ�����ʽ
	 * @param bytes
	 * @return
	 */
	public static String byte2hex(byte[] bytes) {

		StringBuilder stringBuilder = new StringBuilder();
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> params = new HashMap<>();
		params.put("abc", "abc");
		params.put("def", "def");
		String sign = signRequest(params, "Secret@2019#", "md5");
//		logger.info("sign��  ========> " + sign);
		System.out.println(sign);
	}

}
