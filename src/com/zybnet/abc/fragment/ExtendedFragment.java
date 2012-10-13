package com.zybnet.abc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zybnet.abc.R;
import com.zybnet.abc.utils.SlotDetailHelper;
import com.zybnet.abc.view.HistoryViewFlipper;
import com.zybnet.abc.view.SlotView;
import com.zybnet.abc.view.TableView;

public class ExtendedFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle params) {
		Context mActivity = getActivity();
		
		// Setup the timetable view
		TableView table = new TableView(mActivity);
		table.setSlotListener(new SlotListener());
				
		return table;
	}
	
	private class SlotListener implements View.OnClickListener {
		
		@Override
		public void onClick(View view) {
			SlotDetailHelper helper = new SlotDetailHelper(abc());
			helper.fillView((SlotView) view, ExtendedFragment.this);
			
			left().swapRootChild(helper.getView(), R.anim.left_pane_in, R.anim.left_pane_out);
		}
	}

	@Override
	public OnClickListener getSettingsMenuClickedListener() {
		return null;
	}
	
	private HistoryViewFlipper left() {
		return (HistoryViewFlipper) abc().findViewById(R.id.left);
	}
}
