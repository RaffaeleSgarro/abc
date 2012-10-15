package com.zybnet.abc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zybnet.abc.R;
import com.zybnet.abc.view.HistoryViewFlipper;
import com.zybnet.abc.view.NavigateBackView;
import com.zybnet.abc.view.PreferenceView;
import com.zybnet.abc.view.SlotDetailView;
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
			SlotDetailView details = SlotDetailView.create(abc(), (SlotView) view, right());
			left().swapRootChild(details, R.anim.page_in_default, R.anim.page_out_default);
		}
	}

	@Override
	public OnClickListener getSettingsMenuClickedListener() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (right().getCurrentView() instanceof PreferenceView)
					return;
				
				NavigateBackView.Item item = new NavigateBackView.Item(abc());
				item.opener = view;
				item.view = new PreferenceView(abc());
				item.keep = false;
				right().showView(item);
			}
		};
	}
	
	private HistoryViewFlipper right() {
		return (HistoryViewFlipper) abc().findViewById(R.id.right);
	}
	
	private HistoryViewFlipper left() {
		return (HistoryViewFlipper) abc().findViewById(R.id.left);
	}
}
