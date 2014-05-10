package ch.droptilllate.application.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ch.droptilllate.application.os.OSValidator;

public class Configuration {

	/**
	 * Set Propertie DROPBOX path
	 * @param dropboxpath
	 * @param destpath with / at end
	 * @throws IOException
	 */
	public static void setPropertieDropBoxPath(String dropboxpath, String destpath) throws IOException {		 
		Properties configProperty = new Properties();
		configProperty.setProperty("dropboxpath", dropboxpath);
		File file = new File( destpath+"config.properties");
		FileOutputStream fileOut = new FileOutputStream(file,true);
		configProperty.store(fileOut, "Dropboxpath property");
	  }
	
	/**
	 * Set Propertie TEMPPATH path
	 * @param temppath
	 * @param destpath with / at end
	 * @throws IOException
	 */
	public static void setPropertieTempPath(String temppath, String destpath) throws IOException {		 

		Properties configProperty = new Properties();
		configProperty.setProperty("temppath", temppath);
		File file = new File(destpath+"config.properties");
		FileOutputStream fileOut = new FileOutputStream(file,true);
		configProperty.store(fileOut, "Tempfolder property");
	
	  }

	
	public static String getPropertieDropBoxPath(String srcpath , boolean withSlash) {
		 
		Properties prop = new Properties();
		InputStream input = null;
		String path = null;
		try {
	 
			input = new FileInputStream(srcpath +"config.properties");
	 
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


	/**
	 * Get Temp Path
	 * @param srcpath Additional propertie path
	 * @param withSlash
	 * @return path
	 */
	public static String getPropertieTempPath(String srcpath,boolean withSlash) {
		Properties prop = new Properties();
		InputStream input = null;
		String path = null;
		try {
	 
			input = new FileInputStream(srcpath+"config.properties");
	 
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
