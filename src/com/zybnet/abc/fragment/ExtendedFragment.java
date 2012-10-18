package com.zybnet.abc.fragment;

import android.view.View;
import android.view.View.OnClickListener;

import com.zybnet.abc.R;
import com.zybnet.abc.view.HistoryViewFlipper;
import com.zybnet.abc.view.NavigateBackView;
import com.zybnet.abc.view.PreferenceView;
import com.zybnet.abc.view.SlotDetailView;
import com.zybnet.abc.view.SlotView;
import com.zybnet.abc.view.TableView;

public class ExtendedFragment extends BaseFragment {
	
	private class SlotListener implements View.OnClickListener {
		
		@Override
		public void onClick(View view) {
			SlotDetailView details = new SlotDetailView(abc(), right(), (SlotView) view);
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

	@Override
	public void addTable(TableView table) {
		table.setSlotListener(new SlotListener());
		right().addView(table);
	}
}
