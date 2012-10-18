package com.zybnet.abc.view;

import java.sql.Time;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zybnet.abc.R;
import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.fragment.BaseFragment;
import com.zybnet.abc.model.Grade;
import com.zybnet.abc.model.Homework;
import com.zybnet.abc.model.Model;
import com.zybnet.abc.model.Slot;
import com.zybnet.abc.model.Subject;
import com.zybnet.abc.utils.TitleDescriptionAdapter;
import com.zybnet.abc.utils.U;
import com.zybnet.abc.view.EditView.Delegate;
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
			Delegate helper;
			int layout;
			
			switch(v.getId()) {
			case R.id.title:
				helper = new TitleHelper(R.string.edit_displayed, slot.display_text) {
					@Override
					public void afterInflate(EditView view) {
						super.afterInflate(view);
						EditText et = (EditText) view.findViewById(R.id.content);
						et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
					}
					
					@Override
					public void save(EditView view) {
						Slot dst = new Slot(slot);
						dst.display_text = ((TextView) view.findViewById(R.id.content)).getText().toString();
						dst.save(abc.db());
						flipper.back();
					}
				};
				layout = R.layout.edit_item;
				break;
			case R.id.time:
				helper = new Delegate() {
					private TimePicker find(View v, int id) {
						return (TimePicker) v.findViewById(id);
					}
					
					private void setup(View v, int id, Time time, int hours) {
						TimePicker t = find(v, id);
						t.setIs24HourView(true);
						if (time == null) {
							t.setCurrentHour(hours);
							t.setCurrentMinute(0);
						} else {
							t.setCurrentHour(time.getHours());
							t.setCurrentMinute(time.getMinutes());
						}
					}
					
					@Override
					public void afterInflate(EditView view) {
						setup(view, R.id.start, slot.start, 8);
						setup(view, R.id.end, slot.end, 9);
					}
					
					@Override
					public void save(EditView view) {
						Time start = extract(view, R.id.start);
						Time end = extract(view, R.id.end);
						
						Slot dst = new Slot(slot);
						dst.start = start;
						dst.end = end;
						
						dst.save(abc.db());
						flipper.back();
					}
					
					private Time extract(EditView parent, int id) {
						TimePicker src = find(parent, id);
						return new Time(src.getCurrentHour(), src.getCurrentMinute(), 0);
					}
				};
				layout = R.layout.edit_time;
				break;
			case R.id.place:
				helper = new TitleHelper(R.string.edit_place, slot.place){
					@Override
					public void save(EditView view) {
						Slot dst = new Slot(slot);
						dst.place = ((TextView) view.findViewById(R.id.content)).getText().toString();
						dst.save(abc.db());
						flipper.back();
					}
				};
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
	
	@Override
	public void onAttachedToWindow() {
		Model.Channel.subscribe(Slot.class, subscriber);
	}
	
	@Override
	public void onDetachedFromWindow() {
		Model.Channel.unsucribe(Slot.class, subscriber);
	}
	
	private Model.Subscriber subscriber = new Model.Subscriber() {
		
		@Override
		public void onMessage(final Model model) {
			post(new Runnable() {
				public void run() {
					Slot other = (Slot) model;
					Slot self = getSlot();
					
					if (other.ord == self.ord && other.day == self.day) {
						fillView((Slot) model);
					}
				}
			});
		}
	};
	
	private class TitleHelper extends EditView.Delegate {
		public TitleHelper(int stringId, String value) {
			title = getResources().getString(stringId);
			this.value = value;
		}
		
		private String title;
		private String value;
		
		@Override
		public void afterInflate(EditView view) {
			((TextView) view.findViewById(R.id.title)).setText(title);
			((TextView) view.findViewById(R.id.content)).setText(value);
		}
	}
	
	private Slot slot;
	
	public Slot getSlot() {
		return slot;
	}
	
	public void fillView(Slot slot) {
		
		this.slot = slot;
		
		View view = setText(R.id.title,
				U.uppercaseFirstChar(String.format("%tA, slot %d",
				U.getLocalizedDayOfTheWeek(slot.day), slot.ord)));
		view.setOnClickListener(itemListener);
		view.setTag(slot);
		
		setText(R.id.teacher, slot.teacher);
		
		view = setText(R.id.time,
				(slot.start == null || slot.end == null) ?
						null: String.format("%tR - %tR", slot.start, slot.end));
		view.setOnClickListener(itemListener);
		view.setTag(slot);
		
		view = setText(R.id.place, slot.place);
		view.setTag(slot);
		view.setOnClickListener(itemListener);
		
		ViewGroup subject = setText(R.id.subject, slot.subject_name);
		subject.setOnClickListener(indexListener);
		
		View homework = findViewById(R.id.homework);
		View grades = findViewById(R.id.grades);
		
		if (slot.subject_id < 1) {
			homework.setVisibility(View.INVISIBLE);
			grades.setVisibility(View.INVISIBLE);
		} else {
			homework.setVisibility(View.VISIBLE);
			homework.setOnClickListener(indexListener);
			
			grades.setVisibility(View.VISIBLE);
			grades.setOnClickListener(indexListener);
		}
		
	}
	
	private AbbecedarioActivity abc() {
		return (AbbecedarioActivity) getContext();
	}
	
	private OnClickListener indexListener = new OnClickListener() {
		public void onClick(final View view) {
			IndexView<? extends Model> index;
			
			String title = null;
			ListAdapter adapter = null;
			// TODO don't use hardcoded strings
			switch (view.getId()) {
			case R.id.subject:
				title = "Subjects";
				adapter = new TitleDescriptionAdapter(abc,
						abc.db().getSubjects(),
						"name_short", "name");
				/* TODO this is the item picked listener
				index.setListClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Slot dst = new Slot(slot);
						dst.subject_id = id;
						Subject subject = abc().db().getSubject(id);
						dst.display_text = subject.name_short;
						dst.subject_name = abc().db().getSubject(id).name;
						dst.place = subject.default_place;
						dst.save(abc().db());
						abc().getBackButton().back();
					}
				});
				*/
				index = new IndexView<Subject>(flipper, Subject.class, null);
				break;
			case R.id.homework:
				title = "Homework";
				adapter = new TitleDescriptionAdapter(abc,
						abc.db().getHomework(slot.subject_id),
						"due", "description");
				index = new IndexView<Homework>(flipper, Homework.class, null);
				break;
			case R.id.grades:
				title = "Grades";
				abc.db();
				adapter = new TitleDescriptionAdapter(abc,
						abc.db().getGrades(slot.subject_id),
						"date", "description");
				index = new IndexView<Grade>(flipper, Grade.class, null);
				break;
			default:
				throw new IllegalArgumentException();
			}
			
			index.setTitle(title);
			index.setListAdapter(adapter);
			
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
		if (text == null)
			text = getResources().getString(R.string.edit);
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
