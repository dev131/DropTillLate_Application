package ch.droptilllate.application.core;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class KeysGenerator {

	public String getKey(String password, String salt) {
		// get raw key from password and salt
		Key key= null;
		try {
			SecretKeyFactory factory = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1000, 256);
			key = factory.generateSecret(keyspec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
