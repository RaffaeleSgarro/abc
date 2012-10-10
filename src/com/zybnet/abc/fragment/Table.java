package com.zybnet.abc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zybnet.abc.view.TimeTable;

public class Table extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle params) {
		Context mActivity = getActivity();
		FrameLayout root = new FrameLayout(mActivity);
		
		// Setup the timetable view
		TimeTable table = new TimeTable(mActivity);
		table.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		//table.setSlotListener(new SlotListener());
				
		root.addView(table);
				
		return root;
	}
}
