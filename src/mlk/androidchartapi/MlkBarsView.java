package mlk.androidchartapi;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import mlk.androidchartapi.R;

public class MlkBarsView extends MlkView{
	
	public static final int DEFAULT_BAR_FILL_COLOR = 0xff74AC23;
	public static final int DEFAULT_BAR_MARGIN_IN_DP = 10;
	
	private Drawable barsFill;
	private int barsMarginPx;
	
	private ArrayList<MlkBar> bars;

	public MlkBarsView(Context context, AttributeSet attrs) { //importante que este construtor soh tenha estes argumentos
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MlkBarsView);
		
		setBarsFill(a.getDrawable(R.styleable.MlkBarsView_barsFill));
		setBarsMarginInPixels(a.getDimensionPixelSize(R.styleable.MlkBarsView_barsMargin, -1));
	}

	protected void onDraw(Canvas canvas) {
		if(bars==null)
			return;

		int axisY = 0;
		int axisYEnd = getViewHeight()-getLabelsSizeInPixels()-getAxisMarginInPixels()-1;
		int axisXEnd = getViewWidth();
		
		int maxLabelW = 0;
		double maxValue = 0;
		for(int i = 0; i<bars.size(); i++){
			MlkBar bar = bars.get(i);
			
			maxLabelW = Math.max(maxLabelW, (int) textPaint.measureText(bar.getLabel()));
			maxValue = Math.max(maxValue, bar.getValue());
		}
		int axisX = maxLabelW+getAxisMarginInPixels();
		int barX = axisX+1;
		
		if(bars.size() == 1){
			MlkBar bar = bars.get(0);
			int barHeight = axisYEnd-axisY;
			
			int barY = barHeight/3;
			int barYEnd = barY*2;
			int labelYCenter = axisY+(barHeight/2);
			int labelY = labelYCenter + (getLabelsSizeInPixels()/2);
			int labelX = 0;

			canvas.drawText(bar.getLabel(), labelX, labelY, textPaint);
			canvas.drawLine(maxLabelW, labelYCenter, axisX, labelYCenter, axisPaint);
			
			Drawable visualBar = barsFill.getConstantState().newDrawable();
			visualBar.setBounds(barX, barY, axisXEnd, barYEnd);
			visualBar.draw(canvas);
			
		}else{
			
			int barHeight = Math.max((axisYEnd-axisY)/bars.size(), barsMarginPx*2+1);
			
			for(int i = 0; i<bars.size(); i++){
				MlkBar bar = bars.get(i);
	
				int barInit = (barHeight*i)+axisY;
				int barY = (barHeight*i)+barsMarginPx;
				int doubleBarsMargin = 2*barsMarginPx;
				int barYEnd = barY + barHeight - doubleBarsMargin;
				int labelYCenter = barInit+(barHeight/2);
				int labelY = labelYCenter + (getLabelsSizeInPixels()/2);
				int labelX = maxLabelW - (int) textPaint.measureText(bar.getLabel());
				
				double porcentagemBar = 1;
				if(maxValue>0)
					porcentagemBar = bar.getValue() / maxValue;
				
				int barXEnd = (int) (maxLabelW+((axisXEnd - maxLabelW)*porcentagemBar));
				
				if(labelYCenter > axisYEnd)
					break;
				
				canvas.drawText(bar.getLabel(), labelX, labelY, textPaint);
				canvas.drawLine(maxLabelW, labelYCenter, axisX, labelYCenter, axisPaint);
				
				Drawable visualBar = barsFill.getConstantState().newDrawable();
				visualBar.setBounds(barX, barY, barXEnd, barYEnd);
				visualBar.draw(canvas);
			}
		}
		
		if(getValueLabelsAmount()!=0){
			for(int i = 0; i<getValueLabelsAmount()+1; i++){
				String labelValue = getValueLabelFormatter().format((maxValue/getValueLabelsAmount())*i);
				int labelSize = (int) textPaint.measureText(labelValue);
				int pointX = (((axisXEnd - axisX)/getValueLabelsAmount())*i)+axisX;
				int labelX = pointX-(labelSize/2);
				
				canvas.drawText(labelValue, labelX, getViewHeight(), textPaint);
				canvas.drawLine(pointX, axisYEnd, pointX, axisYEnd+getAxisMarginInPixels(), axisPaint);
			}
		}
		
		canvas.drawLine(axisX, axisY, axisX, axisYEnd, axisPaint);
		canvas.drawLine(axisX, axisYEnd, axisXEnd, axisYEnd, axisPaint);
		
	}
	
	public void setBarsFill(Drawable barsFill){
		if(barsFill!=null)
			this.barsFill = barsFill;
		else
			setBarsFill(DEFAULT_BAR_FILL_COLOR);
	}
	
	private ShapeDrawable getShapeWithColor(int barsFillColor){
		ShapeDrawable shape = new ShapeDrawable();
		shape.getPaint().setColor(barsFillColor);
		
		return shape;
	}

	public void setBarsMarginInPixels(int dimensionPx) {
		barsMarginPx = dimensionPx > -1 ? dimensionPx : toPixels(DEFAULT_BAR_MARGIN_IN_DP);
	}
	
	public void setBarsMarginInDP(int dimensionDP) {
		barsMarginPx = toPixels(dimensionDP > -1 ? dimensionDP : DEFAULT_BAR_MARGIN_IN_DP);
	}

	public void setBarsFill(int barsFillColor){
		barsFill = getShapeWithColor(barsFillColor);
	}
	public void setBars(ArrayList<MlkBar> bars) {
		if(bars == null || bars.isEmpty())
			this.bars = null;
		else
			this.bars = bars;
	}
	
	public int getBarsMarginInPixels() {
		return barsMarginPx;
	}
	public Drawable getBarsFill() {
		return barsFill;
	}
	public ArrayList<MlkBar> getBars() {
		return bars;
	}
}
