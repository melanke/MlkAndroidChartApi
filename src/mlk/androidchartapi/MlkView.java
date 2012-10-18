package mlk.androidchartapi;

import mlk.androidchartapi.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public abstract class MlkView extends View{

	public static final int DEFAULT_LABEL_COLOR = 0xff74AC23;
	public static final int DEFAULT_LABEL_SIZE_IN_DP = 16;
	public static final int DEFAULT_VALUE_LABELS_AMOUNT = 5;
	public static final int DEFAULT_AXIS_COLOR = 0xff74AC23;
	public static final int DEFAULT_AXIS_LEFT_MARGIN_IN_DP = 5;
	
	private int labelsColor;
	private int labelsSizePx;
	private int valueLabelsAmount;
	private int axisColor;
	private int axisMarginPx;
	private String sideWayText;
	private MlkValueFormatter valueLabelFormatter = new MlkSimpleFormatter();
	
	private int viewWidth = 0;
	private int viewHeight = 0;
	
	
	
	
	
	protected Paint textPaint = new Paint();
	protected Paint axisPaint = new Paint(); 
	protected int axisX;
	protected int axisXEnd;
	protected int axisY;
	protected int axisYEnd;
	
	
	
	
	public MlkView(Context context, AttributeSet attrs) { //importante que este construtor soh tenha estes argumentos
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MlkView);
		
		setLabelsColor(a.getColor(R.styleable.MlkView_labelsColor, DEFAULT_LABEL_COLOR));
		setLabelsSizeInPixels(a.getDimensionPixelSize(R.styleable.MlkView_labelsSize, -1));
		setAxisColor(a.getColor(R.styleable.MlkView_axisColor, DEFAULT_AXIS_COLOR));
		setAxisMarginInPixels(a.getDimensionPixelSize(R.styleable.MlkView_axisLeftMargin, -1));
		setValueLabelsAmount(a.getInteger(R.styleable.MlkView_valueLabelsAmount, DEFAULT_VALUE_LABELS_AMOUNT));
		setSideWayText(a.getString(R.styleable.MlkView_sidewayText));
		
		textPaint.setAntiAlias(true);
	}
	 
    public void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        
        axisXEnd = getWidth();
        axisYEnd = getHeight();
    }
	
	public int toPixels(int inDp){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inDp, getResources().getDisplayMetrics());
	}
	
	public void setAxisMarginInPixels(int axisLeftMarginPx) {
		this.axisMarginPx = axisLeftMarginPx > -1 ? axisLeftMarginPx : toPixels(DEFAULT_AXIS_LEFT_MARGIN_IN_DP);
	}
	public void setAxisMarginInDP(int dimensionDP) {
		axisMarginPx = toPixels(dimensionDP > -1 ? dimensionDP : DEFAULT_AXIS_LEFT_MARGIN_IN_DP);
	}
	public void setLabelsSizeInPixels(int labelsSizePx) {
		this.labelsSizePx = labelsSizePx > -1 ? labelsSizePx : toPixels(DEFAULT_LABEL_SIZE_IN_DP);
		textPaint.setTextSize(getLabelsSizeInPixels()); 
	}
	public void setLabelsSizeInDP(int dimensionDP) {
		labelsSizePx = toPixels(dimensionDP > -1 ? dimensionDP : DEFAULT_LABEL_SIZE_IN_DP);
		textPaint.setTextSize(getLabelsSizeInPixels()); 
	}

	public void setValueLabelsAmount(int valueLabelsAmount) {
		this.valueLabelsAmount = valueLabelsAmount;
	}
	public void setAxisColor(int axisColor) {
		this.axisColor = axisColor;
		axisPaint.setColor(getAxisColor()); 
	}
	public void setLabelsColor(int labelsColor) {
		this.labelsColor = labelsColor;
		textPaint.setColor(getLabelsColor()); 
	}
	
	

	public int getViewWidth() {
		return viewWidth;
	}

	public int getViewHeight() {
		return viewHeight;
	}

	public int getValueLabelsAmount() {
		return valueLabelsAmount;
	}
	public int getAxisColor() {
		return axisColor;
	}
	public int getAxisMarginInPixels() {
		return axisMarginPx;
	}
	public int getLabelsColor() {
		return labelsColor;
	}
	public int getLabelsSizeInPixels() {
		return labelsSizePx;
	}

	public String getSideWayText() {
		return sideWayText;
	}

	public void setSideWayText(String sideWayText) {
		if(sideWayText==null || sideWayText.length()<=0)
			this.sideWayText = null;
		else
			this.sideWayText = sideWayText;
	}

	public MlkValueFormatter getValueLabelFormatter() {
		return valueLabelFormatter;
	}

	public void setValueLabelFormatter(MlkValueFormatter valueLabelFormatter) {
		this.valueLabelFormatter = valueLabelFormatter;
	}

}
