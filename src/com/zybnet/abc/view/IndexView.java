package com.zybnet.abc.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.utils.U;

@SuppressLint("ViewConstructor")
public class IndexView extends LinearLayout {

	public IndexView(Activity a) {
		super(a);
		setOrientation(LinearLayout.VERTICAL);
		U.setPaddingLeft(this, 10);
		U.setPaddingRight(this, 10);
		a.getLayoutInflater().inflate(R.layout.index, this, true);
	}
	
	protected TextView title() {
		return (TextView) findViewById(R.id.title);
	}
	
	protected ListView list() {
		return (ListView) findViewById(R.id.list);
	}
	
	public void setTitle(String title) {
		title().setText(title);
	}
	
	public void configureEditView(final HistoryViewFlipper switcher,
			final int layout, final EditView.Helper helper) {
		findViewById(R.id.add_new).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View button) {
				EditView view = new EditView(abc(), layout, helper);
				
				NavigateBackView.Item item = new NavigateBackView.Item(abc());
				item.opener = IndexView.this;
				item.view = view;
				item.keep = false;
				
				switcher.showView(item);
			}
		});
	}
	
	public void setListClickListener(AdapterView.OnItemClickListener listener) {
		list().setOnItemClickListener(listener);
	}
	
	public void setListAdapter(ListAdapter adapter) {
		list().setAdapter(adapter);
	}
	
	protected AbbecedarioActivity abc() {
		return (AbbecedarioActivity) getContext();
	}
}
