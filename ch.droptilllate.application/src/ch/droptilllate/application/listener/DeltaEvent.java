package ch.droptilllate.application.listener;

public class DeltaEvent {
	protected Object actedUpon;
	
	public DeltaEvent(Object receiver) {
		this.actedUpon = receiver;
	}
	
	public Object receiver() {
		return this.actedUpon;
	}
}
