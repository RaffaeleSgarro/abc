package com.zybnet.abc.view;

import android.annotation.SuppressLint;
import android.app.Activity;
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
	
	public void setListAdapter(ListAdapter adapter) {
		list().setAdapter(adapter);
	}
	
	protected AbbecedarioActivity abc() {
		return (AbbecedarioActivity) getContext();
	}
	
	
	public static IndexView create(Activity a) {
		IndexView instance = (IndexView) a.getLayoutInflater().inflate(R.layout.index, null, false);
		return instance;
	}
}
