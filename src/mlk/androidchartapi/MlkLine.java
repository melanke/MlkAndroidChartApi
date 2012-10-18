package mlk.androidchartapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;

public class MlkLine {
	
	private float[] values;
	private int lineColor = Integer.MIN_VALUE;
	private Shader areaFill;
	private int gradientTopColor = Integer.MIN_VALUE;
	private int gradientBottomColor = Integer.MIN_VALUE;
	
	//calculated fields
	private float maxValue = 0;
	private int maxLabelWidth = 0;
	
	public MlkLine(float[] values) {
		super();
		setValues(values);
	}
	
	public void setBitmapAreaFill(Context context, int resource){
		//A Bitmap object that is going to be passed to the BitmapShader  
        Bitmap fillBMP;  
       //The shader that renders the Bitmap  
        BitmapShader fillBMPshader;
		//Initialize the bitmap object by loading an image from the resources folder  
        fillBMP = BitmapFactory.decodeResource(context.getResources(), resource);  
        //Initialize the BitmapShader with the Bitmap object and set the texture tile mode  
        fillBMPshader = new BitmapShader(fillBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);  
        setAreaFill(fillBMPshader);
	}

	public float[] getValues() {
		return values;
	}

	public void setValues(float[] values) {
		this.values = values;
		for(float v : this.values){
			maxValue = Math.max(v, maxValue);
		}
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public Shader getAreaFill() {
		return areaFill;
	}

	public void setAreaFill(Shader areaFill) {
		this.areaFill = areaFill;
	}

	public void setGradientColorsAreaFill(int gradientTopColor, int gradientBottomColor) {
		this.gradientTopColor = gradientTopColor;
		this.gradientBottomColor = gradientBottomColor;
	}

	public int getGradientTopColorAreaFill() {
		return gradientTopColor;
	}

	public int getGradientBottomColorAreaFill() {
		return gradientBottomColor;
	}

	public float getMaxValue() {
		return maxValue;
	}
	
	
	
	
	
	
	
	
	
	

}
