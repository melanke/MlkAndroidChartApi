package mlk.androidchartapi;

import java.util.ArrayList;

import mlk.androidchartapi.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;

public class MlkLinesView extends MlkView{
	
	private static final int DEFAULT_LINES_COLOR = 0xFFFFFFFF;
	private static final int DEFAULT_GRADIENTS_TOP_COLOR = 0xFFFFFFFF;
	private static final int DEFAULT_GRADIENTS_BOTTOM_COLOR = 0x00FFFFFF;
	
	private int linesColor;
	private int gradientsTopColor;
	private int gradientsBottomColor;
	
	private ArrayList<MlkLine> lines;
	

	
    private Paint strokePaint = new Paint();
    private Paint fillPaint = new Paint();
    private Path strokePath = new Path(); 
    private Path fillPath = new Path();
    private int maxLabelW = 0;
    private int valuesMaxAmount = 0;
    private float maxValue = Float.NEGATIVE_INFINITY;
    private float horizontalSpaceBetweenPoints = 0;
    private float verticalSpaceBetweenPoints = 0;
	private float spaceBetweenValues = 0;
	private int spaceBetweenPoints = 0;

    

	public MlkLinesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MlkLinesView);

		setLinesColor(a.getColor(R.styleable.MlkLinesView_linesColor, DEFAULT_LINES_COLOR));
		setGradientsTopColor(a.getColor(R.styleable.MlkLinesView_gradientsTopColor, DEFAULT_GRADIENTS_TOP_COLOR));
		setGradientsBottomColor(a.getColor(R.styleable.MlkLinesView_gradientsBottomColor, DEFAULT_GRADIENTS_BOTTOM_COLOR));
		

        fillPaint.setColor(0xFFFFFFFF); 
        fillPaint.setStyle(Paint.Style.FILL);
        strokePaint.setDither(true);  
        strokePaint.setStyle(Paint.Style.STROKE);  
        strokePaint.setAntiAlias(true);  
        strokePaint.setStrokeWidth(toPixels(3)); 
	}
	
	public void setLines(ArrayList<MlkLine> lines) {
		if(lines==null || lines.isEmpty()){
			this.lines = null;
			return;
		}
		
		this.lines = lines;

		maxLabelW = 0;
		valuesMaxAmount = 0;
		maxValue = Float.NEGATIVE_INFINITY;
		
		for(MlkLine line : lines){
			valuesMaxAmount = Math.max(line.getValues().length, valuesMaxAmount);
			maxValue = Math.max(line.getMaxValue(), maxValue);
			if(getValueLabelsAmount()>0){
				for(float value : line.getValues()){
					maxLabelW = Math.max(maxLabelW, (int) textPaint.measureText(getValueLabelFormatter().format(value)+""));
				}
			}
		}
		
		regenerateSpaces();
	}
	
	private void regenerateSpaces(){
		axisYEnd = (int) (getHeight()-(getLabelsSizeInPixels()/2));
		axisX = (getLabelsSizeInPixels()/2) + maxLabelW+getAxisMarginInPixels();
		
		if(getSideWayText()!=null)
			axisX += getLabelsSizeInPixels()+1;
		
		if(valuesMaxAmount < 2 || maxValue<0)//por enquanto s— aceita valores positivos e pelo menos 2 valores
			return;
		
		if(maxValue == 0)
			maxValue = 1;
		
		
		horizontalSpaceBetweenPoints = ((float) (axisXEnd - axisX))/((float) (valuesMaxAmount-1.0));
		verticalSpaceBetweenPoints = (axisYEnd - axisY)/maxValue;
		
		if(getValueLabelsAmount()<1)
			return;
		
		spaceBetweenValues = (maxValue/getValueLabelsAmount());
		spaceBetweenPoints = ((axisYEnd - axisY)/getValueLabelsAmount())-1;
		
		invalidate(); //redraw
	}
	
	protected void onDraw(Canvas canvas) {
		if(lines==null || horizontalSpaceBetweenPoints <= 0 || verticalSpaceBetweenPoints <= 0){
			return;
		}
		
		if(getSideWayText()!=null){
			canvas.save();
			int sidewaysize = (int) textPaint.measureText(getSideWayText());
			canvas.translate(getLabelsSizeInPixels(), getHeight()/2 + sidewaysize/2);
			canvas.rotate(-90);
			canvas.drawText(getSideWayText(), 0, 0, textPaint);
			canvas.restore();
		}
		
		for(MlkLine line : lines){

			strokePath.reset();
			fillPath.reset();
			
	        strokePaint.setColor(getLineColorOfLine(line));  
	        
	        int maxPointY = (int) (axisYEnd-(line.getMaxValue()*verticalSpaceBetweenPoints));
			fillPaint.setShader(getShaderOfLine(line, axisXEnd, axisXEnd, maxPointY, axisYEnd));
			
			int pointX = 0;
			for(float i = 0; i<line.getValues().length; i++){
				float value = line.getValues()[(int) i];
				pointX = Math.round(axisX+(i*horizontalSpaceBetweenPoints));
				int pointY = (int) (axisYEnd-(value*verticalSpaceBetweenPoints));
				
				if(i==0){
					strokePath.moveTo(pointX, pointY);
					fillPath.moveTo(pointX, pointY);
				}else{
					strokePath.lineTo(pointX, pointY);
					fillPath.lineTo(pointX, pointY);
				}
			}
			fillPath.lineTo(pointX, axisYEnd);
			fillPath.lineTo(axisX, axisYEnd);
			
			canvas.drawPath(fillPath, fillPaint);
			canvas.drawPath(strokePath, strokePaint);
		}
		
		if(getValueLabelsAmount()>0){
			for(int i = 0; i<getValueLabelsAmount()+1; i++){
				String labelValue = getValueLabelFormatter().format(spaceBetweenValues*i);
				int pointY = (int) (axisYEnd-(spaceBetweenPoints*i));
				int pointX = axisX - getAxisMarginInPixels();
				int labelY = (int) (pointY+getLabelsSizeInPixels()/2);
				int labelX = pointX - (int) textPaint.measureText(labelValue);
				
				canvas.drawText(labelValue, labelX, labelY, textPaint);
				canvas.drawLine(pointX, pointY, axisX, pointY, axisPaint);
			}
		}
		
		canvas.drawLine(axisX, axisY, axisX, axisYEnd, axisPaint);
		canvas.drawLine(axisX, axisYEnd, axisXEnd, axisYEnd, axisPaint);
	}
	
	private Shader getShaderOfLine(MlkLine line, int x0, int x1, int y0, int y1){
		if(line.getAreaFill()!=null)
			return line.getAreaFill();
		
		if(line.getGradientTopColorAreaFill()==Integer.MIN_VALUE || line.getGradientBottomColorAreaFill()==Integer.MIN_VALUE)
			line.setGradientColorsAreaFill(getGradientsTopColor(), getGradientsBottomColor());
		
		return new LinearGradient(x0, y0, x1, y1, line.getGradientTopColorAreaFill(), line.getGradientBottomColorAreaFill(), Shader.TileMode.MIRROR);
	}
	
	private int getLineColorOfLine(MlkLine line){
		return line.getLineColor()==Integer.MIN_VALUE ? getLinesColor() : line.getLineColor();
	}

	public ArrayList<MlkLine> getLines() {
		return lines;
	}

	public int getLinesColor() {
		return linesColor;
	}

	public void setLinesColor(int linesColor) {
		this.linesColor = linesColor;
	}

	public int getGradientsTopColor() {
		return gradientsTopColor;
	}

	public void setGradientsTopColor(int gradientsTopColor) {
		this.gradientsTopColor = gradientsTopColor;
	}

	public int getGradientsBottomColor() {
		return gradientsBottomColor;
	}

	public void setGradientsBottomColor(int gradientsBottomColor) {
		this.gradientsBottomColor = gradientsBottomColor;
	}
	
	@Override
	public void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        regenerateSpaces();
    }
	
	@Override
	public void setLabelsSizeInPixels(int labelsSizePx){
		super.setLabelsSizeInPixels(labelsSizePx);
		regenerateSpaces();
	}
	
	@Override
	public void setLabelsSizeInDP(int dimensionDP){
		super.setLabelsSizeInDP(dimensionDP);
		regenerateSpaces();
	}
	
	
	
	

}
