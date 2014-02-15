package ch.droptilllate.application.listener;

public interface IDeltaListener {
	public void add(DeltaEvent event);

	public void remove(DeltaEvent event);
}
