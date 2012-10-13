package com.zybnet.abc.utils;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.fragment.BaseFragment;
import com.zybnet.abc.model.Slot;
import com.zybnet.abc.view.SlotView;

public class SlotDetailHelper {
	
	private ViewGroup view;
	
	public SlotDetailHelper(Activity activity) {
		ViewGroup root = new FrameLayout(activity);
		root.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		view = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.slot_detail, root, false);
	}
	
	public void fillView(SlotView slotView, BaseFragment ctx) {
		Slot slot = ctx.db().getSlot(
				slotView.getColumn() + 1,
				slotView.getRow() + 1
		);
		fillView(slot);
	}
	
	public void fillView(Slot slot) {
		setText(R.id.title, U.uppercaseFirstChar(String.format("%tA, slot %d",
				U.getLocalizedDayOfTheWeek(slot.day), slot.ord)));
		setText(R.id.subject, slot.subject_name);
		setText(R.id.teacher, slot.teacher);
		setText(R.id.time, String.format("%tR - %tR", slot.start, slot.end));
		setText(R.id.room, slot.where);
		// TODO what about listeners?
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
