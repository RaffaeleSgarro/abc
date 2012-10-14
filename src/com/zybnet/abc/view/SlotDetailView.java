package com.zybnet.abc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.fragment.BaseFragment;
import com.zybnet.abc.model.Slot;
import com.zybnet.abc.utils.TitleDescriptionAdapter;
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
		setText(R.id.teacher, slot.teacher);
		setText(R.id.time, String.format("%tR - %tR", slot.start, slot.end));
		setText(R.id.place, slot.where);
		
		// TODO set tag
		ViewGroup subject = setText(R.id.subject, slot.subject_name);
		subject.setOnClickListener(indexListener);
		
		View homework = findViewById(R.id.homework);
		homework.setOnClickListener(indexListener);
		homework.setTag(slot.subject_id);
		
		View grades = findViewById(R.id.grades);
		grades.setOnClickListener(indexListener);
		grades.setTag(slot.subject_id);
	}
	
	private OnClickListener indexListener = new OnClickListener() {
		public void onClick(final View view) {
			final String t;
			final ListAdapter a;
			
			switch (view.getId()) {
			case R.id.subject:
				t = "Subjects";
				a = new TitleDescriptionAdapter(abc,
						abc.db().getSubjects(),
						"name_short", "name");
				break;
			case R.id.homework:
				t = "Homework";
				a = new TitleDescriptionAdapter(abc,
						abc.db().getHomework((Integer) view.getTag()),
						"due", "description");
				break;
			case R.id.grades:
				t = "Grades";
				abc.db();
				a = new TitleDescriptionAdapter(abc,
						abc.db().getGrades((Integer) view.getTag()),
						"date", "description");
				break;
			default:
				t = null;
				a = null;
			}
			
			IndexView index = IndexView.create(
					(AbbecedarioActivity) getContext(),
					new IndexView.Content() {
						{
							title = t;
							adapter = a;
						}
					});
			flipper.showView(index);
		}
	};
	
	private HistoryViewFlipper flipper;
	
	private ViewGroup group(int id) {
		return (ViewGroup) findViewById(id);
	}
	
	private ViewGroup setText(int id, String text) {
		ViewGroup layout = group(id);
		((TextView) layout.findViewById(R.id.content)).setText(text);
		return layout;
	}
	
	public static SlotDetailView create(AbbecedarioActivity a, HistoryViewFlipper flipper) {
		if (flipper == null)
			throw new NullPointerException("We need a HistoryViewFlipper");
		
		SlotDetailView instance = (SlotDetailView) a.getLayoutInflater().inflate(R.layout.slot_detail, null, false);
		instance.flipper = flipper;
		instance.abc = a;
		return instance;
	}
	
	private AbbecedarioActivity abc;
	
	public static SlotDetailView create(AbbecedarioActivity a, SlotView src, HistoryViewFlipper flipper) {
		SlotDetailView detail = create(a, flipper);
		detail.fillView(src, a.getActiveFragment());
		return detail;
	}
	
}
