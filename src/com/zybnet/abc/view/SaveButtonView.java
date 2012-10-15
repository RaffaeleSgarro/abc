package com.zybnet.abc.view;

import com.zybnet.abc.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class SaveButtonView extends ImageButton {

	public SaveButtonView(Context context) {
		super(context);
	}
	
	public SaveButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SaveButtonView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View saveButton) {
			startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha_out));
			editView.save();
			editView = null;
			setOnClickListener(null);
		}
	};
	
	private EditView editView;
	
	public void setEditView(EditView editView) {
		setVisibility(View.VISIBLE);
		startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in));
		this.editView = editView;
		setOnClickListener(listener);
	}
	
	public void unsetEditView() {
		editView = null;
		startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha_out));
		setOnClickListener(null);
	}
}
