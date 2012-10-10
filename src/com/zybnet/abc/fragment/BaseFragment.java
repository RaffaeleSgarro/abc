package com.zybnet.abc.fragment;

import android.support.v4.app.Fragment;

import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.view.TimeTable;

public class BaseFragment extends Fragment {
	
	/*
	 * Convenience method (also short-named) to get the underlying
	 * activity casted to AbbecedarioActivity.
	 * 
	 * Note that getActivity() may return null
	 * 
	 */
	public AbbecedarioActivity abc() {
		return (AbbecedarioActivity) getActivity();
	}
	
	public TimeTable getTableView() {
		return (TimeTable) abc().getTableFragment().getView().findViewById(TimeTable.ID);
	}
}
