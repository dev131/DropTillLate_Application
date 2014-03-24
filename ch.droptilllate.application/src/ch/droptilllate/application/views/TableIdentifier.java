package ch.droptilllate.application.views;

public enum TableIdentifier {

	NAME(395), TYPE(90), SIZE(70), DATE(85);

	public int columnWidth;

	private TableIdentifier(int width) {
		this.columnWidth = width;
	}
}
