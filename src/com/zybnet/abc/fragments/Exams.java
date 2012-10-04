package com.zybnet.abc.fragments;

import com.zybnet.abc.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class Exams extends ListFragment {
	
	@Override
	public void onCreate(Bundle params) {
		super.onCreate(params);
		String[] exams = {"Lorem", "Ipsum", "Dolor", "Sit Amen"};
		setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.exam, R.id.name, exams));
	}
}
