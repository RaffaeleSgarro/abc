package com.zybnet.abc.view;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;

import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.U;

@SuppressLint({"ViewConstructor" })
public class EditView extends LinearLayout {

	private SaveButtonView save;
	private NavigateBackView back;
	
	private AbbecedarioActivity abc;
	private Helper helper;
	
	public EditView(AbbecedarioActivity abc, int layout, Helper helper) {
		super(abc);
		
		setOrientation(LinearLayout.VERTICAL);
		U.setPaddingLeft(this, 10);
		U.setPaddingRight(this, 10);
		
		this.abc = abc;
		this.helper = helper;
		
		abc.getLayoutInflater().inflate(layout, this, true);
		helper.afterInflate(this);
		
		save = abc.getSaveButton();
		back = abc.getBackButton();
	}
	
	@Override
	public void onAttachedToWindow() {
		save.setEditView(this);
	}
	
	public void save() {
		save.setEditView(this);
		helper.persist(this, abc.db());
		back.back();
	}

	public static class Helper {
		public void afterInflate(EditView view) {}
		public void persist(EditView view, DatabaseHelper db) {}
	}
	
	@Override
	public void onDetachedFromWindow() {
		save.unsetEditView();
	}
}
