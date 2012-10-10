package com.zybnet.abc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zybnet.abc.R;

public class Slot extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
		View slot_detail = inflater.inflate(R.layout.slot_detail, container, false);
		return slot_detail;
	}
	
}
