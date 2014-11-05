package com.uperone.drawview;

import android.view.MotionEvent;
import android.view.View;

import com.uperone.drawview.view.DrawView;


public class DrawMainActivity extends BaseActivity {
	@Override
	public void setContentView() {
		setContentView(R.layout.activity_draw_main_layout);
	}

	@Override
	public void findViews() {
		mDrawView = ( DrawView )findViewById(R.id.drawViewId);
	}

	@Override
	public void getData() {
		
	}

	@Override
	public void showContent() {
		
	}
	
	public void onClick( View v ){
		switch( v.getId( ) ){
		case R.id.clearBtnId:{
			mDrawView.clear( );
		}
		break;
		default:{
			
		}
		break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int pointCnt = event.getPointerCount( );
    	for( int index = 0; index < pointCnt; index++ ){
    		if( mDrawView.inArea( event.getX( index ) - mDrawView.getLeft( ), event.getY( index ) - mDrawView.getTop( ) ) ){
    			mDrawView.onTouchEvent( event );
    			System.out.println( "action === pointIndex " + index );
    			return false;
    		}
    	} 

    	mDrawView.setInView( false );
    	return super.onTouchEvent(event);
	}
	
	
	private DrawView mDrawView = null;
}
