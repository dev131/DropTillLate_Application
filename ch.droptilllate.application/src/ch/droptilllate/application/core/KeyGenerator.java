package ch.droptilllate.application.core;

import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {

	public String generateKey(String password, String salt) throws Exception {
		// get raw key from password and salt
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000,128);
		Key key = factory.generateSecret(keyspec);
		System.out.println(key.getClass().getName());
		System.out.println(Arrays.toString(key.getEncoded()));
			
		String value = key.toString();
		return value;
	}
}
