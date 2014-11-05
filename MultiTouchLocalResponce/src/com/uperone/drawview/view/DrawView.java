package com.uperone.drawview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	public DrawView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaintView( );
	}

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaintView( );
	}

	public DrawView(Context context) {
		super(context);
		initPaintView( );
	}
	
	public void clear() {  
        if (null != mPath) {  
            mPath.reset();  
            invalidate();  
        }  
    }  
  
    private void initPaintView() {  
        mPaint.setAntiAlias(true);  
        mPaint.setColor(Color.WHITE);  
        mPaint.setStyle(Paint.Style.STROKE);  
        mPaint.setStrokeJoin(Paint.Join.ROUND);  
        mPaint.setStrokeWidth(5f);  
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	mViewWidth = MeasureSpec.getSize(widthMeasureSpec);   //获取ViewGroup宽度    
    	mViewHeight = MeasureSpec.getSize(heightMeasureSpec);  //获取ViewGroup高度
    }
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        canvas.drawPath(mPath, mPaint);  
    }  
    
    public boolean inArea( float x, float y ){
    	return ( x >= 0 && x <= mViewWidth && y >= 0 && y <= mViewHeight)?true:false;
    }
  
    @SuppressWarnings("deprecation")
	@Override  
    public boolean onTouchEvent(MotionEvent event) {
    	float eventX = -1;  
        float eventY = -1;
        int pointId = 0;
    	int pointCnt = event.getPointerCount( );
    	for( int index = 0; index < pointCnt; index++ ){
    		if( inArea( event.getX( index ) - getLeft( ), event.getY( index ) - getTop( ) ) ){
    			pointId = index;
    			eventX = event.getX( index ) - getLeft( );
    			eventY = event.getY( index ) - getTop( );
    			break;
    		}
    	}
        
    	if( ( eventX == -1 || eventY == -1 ) || ( eventX == 0 || eventY == 0 ) ){
    		return false;
    	}
    	
        switch (event.getAction()) {
        case MotionEvent.ACTION_POINTER_1_DOWN:
        case MotionEvent.ACTION_POINTER_2_DOWN:
        case MotionEvent.ACTION_POINTER_3_DOWN:
        case MotionEvent.ACTION_DOWN: {  
            mPath.moveTo(eventX, eventY);
            mLastTouchX = eventX;  
            mLastTouchY = eventY;
            mInView = true;
            invalidate( );
        }  
            return true;  
        case MotionEvent.ACTION_MOVE:{
        	if( !mInView ){
        		mInView = true;
        		mLastTouchX = eventX;  
                mLastTouchY = eventY;
                mPath.moveTo(eventX, eventY);
        	}
        	drawView( event, eventX, eventY, pointId );
        }
        break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_1_UP:
        case MotionEvent.ACTION_POINTER_2_UP:
        case MotionEvent.ACTION_POINTER_3_UP:{  
        	drawView( event, eventX, eventY, pointId );
        }  
            break;  
        default:  
            return false;  
        }  
  
        return true;  
    }
    
    public void setInView( boolean inView ){
    	mInView = inView;
    }
    
    private void drawView( MotionEvent event, float eventX, float eventY, int pointId ){
    	resetDirtyRect(eventX, eventY);  
        int historySize = event.getHistorySize();  
        for (int i = 0; i < historySize; i++) {  
            float historicalX = event.getHistoricalX(pointId,i) - getLeft( );  
            float historicalY = event.getHistoricalY(pointId,i) - getTop( );  
            getDirtyRect(historicalX, historicalY);  
            mPath.lineTo(historicalX, historicalY);  
        }  

        mPath.lineTo(eventX, eventY);  
        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH),  
                (int) (mDirtyRect.top - HALF_STROKE_WIDTH),  
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH),  
                (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
        
        mLastTouchX = eventX;  
        mLastTouchY = eventY;
    }
  
    private void getDirtyRect(float historicalX, float historicalY) {  
        if (historicalX < mDirtyRect.left) {  
            mDirtyRect.left = historicalX;  
        } else if (historicalX > mDirtyRect.right) {  
            mDirtyRect.right = historicalX;  
        }  
        if (historicalY < mDirtyRect.top) {  
            mDirtyRect.top = historicalY;  
        } else if (historicalY > mDirtyRect.bottom) {  
            mDirtyRect.bottom = historicalY;  
        }  
    }  
  
    private void resetDirtyRect(float eventX, float eventY) {  
        mDirtyRect.left = Math.min(mLastTouchX, eventX);  
        mDirtyRect.right = Math.max(mLastTouchX, eventX);  
        mDirtyRect.top = Math.min(mLastTouchY, eventY);  
        mDirtyRect.bottom = Math.max(mLastTouchY, eventY);  
    }  
  
    private static final float STROKE_WIDTH = 5f;  
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;  
    private float mLastTouchX = 0;  
    private float mLastTouchY = 0;
    
    private boolean mInView = false;
    private int mViewWidth = 0;
    private int mViewHeight = 0;
  
    private final RectF mDirtyRect = new RectF();  
    private Paint mPaint = new Paint();  
    private Path mPath = new Path();  
}
