package ch.droptilllate.application.os;

public class OSValidator {
	 
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String Slash;
 
	public static boolean isWindows() {
 
		return (OS.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}
 
	public static boolean isSolaris() {
 
		return (OS.indexOf("sunos") >= 0);
 
	}
	public static String getSlash(){
		if (isWindows()) {
			Slash = "\\";
		} else if (isMac()) {
			Slash="/";
		} else if (isUnix()) {
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		return Slash;
	}
	
	public static String getSlashForSplit(){
		if (isWindows()) {
			Slash = "\\\\";
		} else if (isMac()) {
			Slash="/";
		} else if (isUnix()) {
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		return Slash;
	}
 
}