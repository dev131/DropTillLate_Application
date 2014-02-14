package ch.droptilllate.application.core;

import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class KeysGenerator {

	public String getKey(String password, String salt) throws Exception {
		// get raw key from password and salt
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 256);
		Key key = factory.generateSecret(keyspec);
		
		// Length
		byte[] keyBytes = key.getEncoded();
        int numBytes = keyBytes.length;
        //TO String
	
		System.out.println("Classname: "+key.getClass().getName());
		System.out.println("Encoded: "+(Arrays.toString(key.getEncoded())));
		System.out.println("Length: "+numBytes);
		System.out.println("Format: " + key.getFormat());
		System.out.println("Algorithm: " + key.getAlgorithm());				
		return Arrays.toString(key.getEncoded());
	}	
}
