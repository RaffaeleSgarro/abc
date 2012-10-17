package com.zybnet.abc.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zybnet.abc.R;
import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.U;

@SuppressLint({"ViewConstructor" })
public class EditView extends RelativeLayout {
	
	private NavigateBackView back;
	
	private AbbecedarioActivity abc;
	private Helper helper;
	
	public EditView(AbbecedarioActivity abc, int layout, Helper helper) {
		super(abc);
		
		U.setPaddingLeft(this, 10);
		U.setPaddingRight(this, 10);
		
		this.abc = abc;
		this.helper = helper;
		
		LayoutInflater infl = abc.getLayoutInflater();
		
		infl.inflate(R.layout.edit_view,this);
		
		ViewGroup container = (ViewGroup) findViewById(R.id.container);
		infl.inflate(layout, container);
		
		helper.afterInflate(this);
		
		back = abc.getBackButton();
		
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
		helper.save(this, abc.db());
		back.back();
	}
	
	private void delete() {
		helper.delete(this, abc.db());
		back.back();
	}

	public static class Helper {
		public void afterInflate(EditView view) {}
		public void save(EditView view, DatabaseHelper db) {}
		public void delete(EditView view, DatabaseHelper db) {}
	}
}
