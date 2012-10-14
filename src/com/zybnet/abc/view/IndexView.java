package com.zybnet.abc.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.activity.AbbecedarioActivity;

public class IndexView extends LinearLayout {

	public IndexView(Context context) {
		super(context);
	}
	
	public IndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	protected TextView title() {
		return (TextView) findViewById(R.id.title);
	}
	
	protected ListView list() {
		return (ListView) findViewById(R.id.list);
	}
	
	private void setTitle(String title) {
		title().setText(title);
	}
	
	private void setupList(ListAdapter adapter) {
		list().setAdapter(adapter);
	}
	
	protected AbbecedarioActivity abc() {
		return (AbbecedarioActivity) getContext();
	}
	
	public void setup(Content c) {
		setTitle(c.title);
		setupList(c.adapter);
	}
	
	public static class Content {
		public String title;
		public ListAdapter adapter;
	}
	
	public static IndexView create(Activity a, Content content) {
		IndexView instance = (IndexView) a.getLayoutInflater().inflate(R.layout.index, null, false);
		instance.setup(content);
		return instance;
	}
}
