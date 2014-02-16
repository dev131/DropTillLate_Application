package ch.droptilllate.application.com;

public class DropBox implements IDropBox{

	@Override
	public String getSharedMail(String name) {
		String mail = null;
		if(name.equals("Person1"))mail="Person1@mail.com";
		if(name.equals("Person2"))mail="Person2@mail.com";
		if(name.equals("Person3"))mail="Person3@mail.com";
		return mail;
	}

}
