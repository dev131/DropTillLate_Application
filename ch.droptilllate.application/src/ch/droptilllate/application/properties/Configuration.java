package ch.droptilllate.application.properties;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import ch.droptilllate.application.lifecycle.OSValidator;

public class Configuration {

	public static void setPropertieDropBoxPath(String dropboxpath) throws IOException {		 
		Properties configProperty = new Properties();
		configProperty.setProperty("dropboxpath", dropboxpath);
		File file = new File("config.properties");
		FileOutputStream fileOut = new FileOutputStream(file,true);
		configProperty.store(fileOut, "Dropboxpath property");
	  }
	
	public static void setPropertieTempPath(String temppath) throws IOException {		 

		Properties configProperty = new Properties();
		configProperty.setProperty("temppath", temppath);
		File file = new File("config.properties");
		FileOutputStream fileOut = new FileOutputStream(file,true);
		configProperty.store(fileOut, "Tempfolder property");
	
	  }

	
	public static String getPropertieDropBoxPath(boolean withSlash) {
		 
		Properties prop = new Properties();
		InputStream input = null;
		String path = null;
		try {
	 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			path = (prop.getProperty("dropboxpath"));
	 
		} catch (IOException ex) {
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
		if(withSlash)return path+ OSValidator.getSlash();
		return path;
	  }


	public static String getPropertieTempPath(boolean withSlash) {
		Properties prop = new Properties();
		InputStream input = null;
		String path = null;
		try {
	 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			path = prop.getProperty("temppath");
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(withSlash)return path+ OSValidator.getSlash();
		return path;
	}
}
