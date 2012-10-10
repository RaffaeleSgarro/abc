package com.zybnet.abc.utils;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.model.Slot;

public class SlotDetailHelper {
	
	private ViewGroup view;
	
	public SlotDetailHelper(Activity activity) {
		ViewGroup root = new FrameLayout(activity);
		root.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		view = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.slot_detail, root, false);
	}
	
	public void fillView(Slot slot) {
		setText(R.id.title, String.format("%s, slot %d", slot.toString(), slot.ord));
		setText(R.id.subject, slot.subject_name);
	}

	public ViewGroup getView() {
		return view;
	}
	
	private void setText(int id, String text) {
		((TextView) view.findViewById(id)).setText(text);
	}
	
	public void attachListener() {
		// TODO
	}
	
	public void detachListener() {
		// TODO
	}
}
