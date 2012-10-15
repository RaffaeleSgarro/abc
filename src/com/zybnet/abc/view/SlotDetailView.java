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
import com.zybnet.abc.view.EditView.Helper;
import com.zybnet.abc.view.NavigateBackView.Item;

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
	
	private OnClickListener itemListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Helper helper;
			int layout;
			
			
			switch(v.getId()) {
			case R.id.title:
				helper = new TitleHelper(R.string.edit_displayed);
				layout = R.layout.edit_item;
				break;
			case R.id.time:
				helper = new Helper();
				layout = R.layout.edit_time;
				break;
			case R.id.place:
				helper = new TitleHelper(R.string.edit_place);
				layout = R.layout.edit_item;
				break;
			default:
				throw new RuntimeException("Register listener for unknown ID");
			}
			
			Item item = new Item(getContext());
			item.opener = SlotDetailView.this;
			item.keep = false;
			item.view = new EditView(abc(), layout, helper);
			flipper.showView(item);
		}
	};
	
	private class TitleHelper extends Helper {
		public TitleHelper(int stringId) {
			title = getResources().getString(stringId);
		}
		
		private String title;
		
		@Override
		public void afterInflate(EditView view) {
			((TextView) view.findViewById(R.id.title)).setText(title);
		}
	}
	
	public void fillView(Slot slot) {
		setText(R.id.title,
				U.uppercaseFirstChar(String.format("%tA, slot %d",
				U.getLocalizedDayOfTheWeek(slot.day), slot.ord)))
				.setOnClickListener(itemListener);
		
		setText(R.id.teacher, slot.teacher);
		setText(R.id.time, String.format("%tR - %tR", slot.start, slot.end))
			.setOnClickListener(itemListener);
		
		setText(R.id.place, slot.where).setOnClickListener(itemListener);
		
		ViewGroup subject = setText(R.id.subject, slot.subject_name);
		subject.setOnClickListener(indexListener);
		
		View homework = findViewById(R.id.homework);
		homework.setOnClickListener(indexListener);
		homework.setTag(slot.subject_id);
		
		View grades = findViewById(R.id.grades);
		grades.setOnClickListener(indexListener);
		grades.setTag(slot.subject_id);
	}
	
	private AbbecedarioActivity abc() {
		return (AbbecedarioActivity) getContext();
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
			NavigateBackView.Item item = new NavigateBackView.Item(getContext());
			item.opener = SlotDetailView.this;
			item.view = index;
			flipper.showView(item);
		}
	};
	
	// This is the right flipper
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
