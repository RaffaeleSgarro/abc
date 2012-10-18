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
	
	private Delegate helper;
	
	public EditView(Context ctx, int layout, Delegate helper) {
		super(ctx);
		
		U.setPaddingLeft(this, 10);
		U.setPaddingRight(this, 10);
		
		this.helper = helper;
		
		LayoutInflater infl = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		infl.inflate(R.layout.edit_view, this);
		
		ViewGroup container = (ViewGroup) findViewById(R.id.container);
		infl.inflate(layout, container);
		
		helper.afterInflate(this);
		
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				save();
			}
		});
		
		findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				delete();
			}
		});
	}
	
	private void save() {
		helper.save(this);
	}
	
	private void delete() {
		helper.delete(this);
	}

	public static class Delegate {
		public void afterInflate(EditView view) {}
		public void save(EditView view) {}
		public void delete(EditView view) {}
	}
}
