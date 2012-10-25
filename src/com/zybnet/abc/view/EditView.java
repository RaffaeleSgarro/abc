package com.zybnet.abc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.U;

@SuppressLint({"ViewConstructor" })
public class EditView extends RelativeLayout {
	
	private Delegate delegate;
	
	public EditView(Context ctx, int layout, Delegate delegate) {
		super(ctx);
		
		U.setPaddingLeft(this, 10);
		U.setPaddingRight(this, 10);
		
		if (delegate == null)
			delegate = new Delegate();
		
		this.delegate = delegate;
		
		LayoutInflater infl = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		infl.inflate(R.layout.edit_view, this);
		
		ViewGroup container = (ViewGroup) findViewById(R.id.container);
		infl.inflate(layout, container);
		
		delegate.afterInflate(this);
		
		findViewById(R.id.save).setOnClickListener(saveListener);
		
		findViewById(R.id.delete).setOnClickListener(deleteListener);
	}

	private OnClickListener saveListener = new OnClickListener() {			
		@Override
		public void onClick(View v) {
			delegate.save(EditView.this);
		}
	};
	
	private OnClickListener deleteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			delegate.delete(EditView.this);
		}
	};
	
	public static class Delegate {
		public void afterInflate(EditView view) {}
		public void save(EditView view) {}
		public void delete(EditView view) {}
	}
}
