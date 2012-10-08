package com.zybnet.abc.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zybnet.abc.view.ExamsListView;

public class Exams extends ListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle params) {
		return new ExamsListView(getActivity());
	}
}
