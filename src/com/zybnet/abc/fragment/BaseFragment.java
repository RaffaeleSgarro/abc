package com.zybnet.abc.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.view.TableView;

public abstract class BaseFragment extends Fragment {
	
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
	
	public TableView getTableView() {
		return abc().getTableView();
	}
	
	public DatabaseHelper db() {
		return abc().db();
	}
	
	public Animation loadAnimation(int id) {
		return AnimationUtils.loadAnimation(getActivity(), id);
	}
	
	public abstract View.OnClickListener getSettingsMenuClickedListener();
	
	public abstract void addTable(TableView table);
}
