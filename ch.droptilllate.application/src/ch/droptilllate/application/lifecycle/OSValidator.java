package ch.droptilllate.application.lifecycle;

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
			System.out.println("This is Windows");
			Slash = "\\";
		} else if (isMac()) {
			System.out.println("This is Mac");
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