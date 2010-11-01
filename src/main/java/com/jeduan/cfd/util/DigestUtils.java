package com.jeduan.cfd.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {
	private DigestUtils() {
	}

	public static String md5(byte[] input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to find MD5");
		}
		
		md.update(input);
		byte[] digest = md.digest();
		BigInteger bigInteger = new BigInteger(1, digest);
		return bigInteger.toString(16);
	}
	
	public static String sha1(byte[] input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to find SHA-1");
		}
		
		md.update(input);
		byte[] digest = md.digest();
		BigInteger bigInteger = new BigInteger(1, digest);
		return bigInteger.toString(16); 
	}
	
}
