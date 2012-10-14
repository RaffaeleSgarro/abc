package com.zybnet.abc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.fragment.BaseFragment;
import com.zybnet.abc.model.Slot;
import com.zybnet.abc.utils.U;

public class SlotDetailView extends LinearLayout {

	public SlotDetailView(Context ctx) {
		super(ctx);
	}
	
	public SlotDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
	}
	
	private void setText(int id, String text) {
		((TextView) findViewById(id)).setText(text);
	}
	
	public static SlotDetailView create(AbbecedarioActivity a) {
		return (SlotDetailView) a.getLayoutInflater().inflate(R.layout.slot_detail, null, false);
	}
	
	public static SlotDetailView create(AbbecedarioActivity a, SlotView src) {
		SlotDetailView detail = create(a);
		detail.fillView(src, a.getActiveFragment());
		return detail;
	}
	
}
