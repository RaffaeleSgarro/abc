package com.zybnet.abc.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zybnet.abc.view.Cell;

public class TimeTable extends Fragment {

	private int rows, columns;
	
	public TimeTable(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
		Activity mActivity = getActivity();
		final int FILL = ViewGroup.LayoutParams.FILL_PARENT;
		LinearLayout root = new LinearLayout(mActivity);
		root.setLayoutParams( new ViewGroup.LayoutParams(FILL, FILL));
		root.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(FILL, FILL, 1f/rows);
		LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(FILL, FILL, 1f/columns);
		
		int[][] bgColors = new int[2][2];
		bgColors[0][0] = Color.parseColor("#e5e5e5"); // grey
		bgColors[0][1] = Color.rgb(255, 255, 255); // white
		bgColors[1][0] = Color.parseColor("#e5cbe5"); // purple
		bgColors[1][1] = Color.parseColor("#ffe5ff"); //pink
		
		final int grey = Color.parseColor("#555555"); // for text
		
		for (int i = 0; i < rows; i ++) {
			LinearLayout row = new LinearLayout(mActivity);
			row.setOrientation(LinearLayout.HORIZONTAL); // default, but anyway
			for (int j = 0; j < columns; j++) {
				Cell cell = new Cell(mActivity, "FOO", bgColors[i % 2][j % 2]);
				cell.setTextColor(grey);
				cell.setGravity(Gravity.CENTER);
				row.addView(cell, cellParams);
			}
			root.addView(row, rowParams);
		}
		return root;
	}
}
