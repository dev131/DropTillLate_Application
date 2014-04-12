package ch.droptilllate.application.provider;

public enum TableIdentifierShare {

	NAME(200), SHARE(80);

	public int columnWidth;

	private TableIdentifierShare(int width) {
		this.columnWidth = width;
	}
}

