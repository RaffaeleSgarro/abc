package com.zybnet.abc.controller;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.model.Subject;
import com.zybnet.abc.model.Teacher;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.IndexController;
import com.zybnet.abc.utils.TitleDescriptionAdapter;
import com.zybnet.abc.view.EditView;
import com.zybnet.abc.view.HistoryViewFlipper;
import com.zybnet.abc.view.IndexView;
import com.zybnet.abc.view.IndexView.OnItemPickedListener;
import com.zybnet.abc.view.NavigateBackView;

public class SubjectController extends IndexController<Subject> {

	private HistoryViewFlipper flipper;
	
	public SubjectController(Context ctx, DatabaseHelper dh, HistoryViewFlipper flipper) {
		super(ctx, dh);
		this.flipper = flipper;
	}

	@Override
	protected CursorAdapter getAdapter(Cursor cursor) {
		return new TitleDescriptionAdapter(ctx, cursor, "name_short", "name");
	}

	@Override
	protected Cursor getCursor() {
		return dh.getSubjects();
	}
	
	@Override
	public void fixAfterInflate(final EditView view, final Subject subject) {
		final TextView tv = (TextView) view.findViewById(R.id.teacher);
		
		tv.setFocusable(false);
		tv.setFocusableInTouchMode(false);
		
		Teacher teacher = dh.fill(Teacher.class, subject.default_teacher_id);
		tv.setText(teacher.name);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				IndexView<Teacher> index = new IndexView<Teacher>((AbbecedarioActivity) ctx, Teacher.class, new TeacherController(ctx, dh));
				index.setOnItemPickedListener(new OnItemPickedListener<Teacher>() {

					@Override
					public void onItemPicked(Teacher t) {
						subject.default_teacher_id = t._id;
						tv.setText(t.name);
						flipper.back();
					}
				});
				
				NavigateBackView.Item item = new NavigateBackView.Item(ctx);
				item.opener = view;
				item.view = index;
				flipper.showView(item);
			}
		});
	}
	
	@Override
	public void fixBeforeSave(EditView view, Subject subject) {
		// TODO
	}
}
