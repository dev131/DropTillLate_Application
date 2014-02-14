package ch.droptilllate.application.core;


public class KeyManager {
	public static void main(String [] args)
	{
		String password = "marco131";
		String salt = "filename";
		KeysGenerator gen = new KeysGenerator();
		try {
			String key = gen.getKey(password, salt);
			System.out.println(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(i);
	}

}
