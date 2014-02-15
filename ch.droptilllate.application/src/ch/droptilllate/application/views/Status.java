package ch.droptilllate.application.views;

import java.util.Observable;

import ch.droptilllate.application.controller.ViewController;

public class Status extends Observable{
	private String message;
	private static Status instance = null;
	public Status() {
		// Exists only to defeat instantiation.
	}

	public static Status getInstance() {
		if (instance == null) {
			instance = new Status();
		}
		return instance;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		setChanged();
		notifyObservers(message);
	}
	
	
}
