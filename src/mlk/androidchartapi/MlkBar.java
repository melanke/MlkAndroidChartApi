package mlk.androidchartapi;

public class MlkBar {
	
	private double value;
	private String label;
	
	
	
	public MlkBar(double value) {
		super();
		this.value = value;
	}

	public MlkBar(double value, String label) {
		super();
		this.value = value;
		this.label = label;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	

}
