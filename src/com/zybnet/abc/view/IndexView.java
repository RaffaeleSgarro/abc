package com.zybnet.abc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zybnet.abc.R;
import com.zybnet.abc.model.Model;
import com.zybnet.abc.utils.U;

@SuppressLint("ViewConstructor")
public class IndexView<T extends Model> extends LinearLayout {

	private TextView title;
	private ListView list;
	private HistoryViewFlipper flipper;
	private Class<T> modelClassToken;
	private EditView.Delegate delegate;
	
	public IndexView(HistoryViewFlipper flipper, Class<T> token, EditView.Delegate delegate) {
		super(flipper.getContext());
		
		this.flipper = flipper;
		this.modelClassToken = token;
		this.delegate = delegate;
		
		setOrientation(LinearLayout.VERTICAL);
		
		U.setPaddingLeft(this, 10);
		U.setPaddingRight(this, 10);
		
		LayoutInflater inflater = (LayoutInflater) flipper.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.index, this, true);
		
		title = (TextView) findViewById(R.id.title);
		list = (ListView) findViewById(R.id.list);
		findViewById(R.id.add_new).setOnClickListener(addNewListener);
	}
	
	public void setTitle(String str) {
		title.setText(str);
	}
	
	public void setListAdapter(ListAdapter adapter) {
		list.setAdapter(adapter);
		list.setOnItemClickListener(itemClickListener);
		list.setOnItemLongClickListener(itemLongClickListener);
	}
	
	private EditView getEditView(long id) {
		int layout = getResources().getIdentifier(
				"edit_" + modelClassToken.getSimpleName().toLowerCase(),
				"layout",
				getContext().getPackageName());
		EditView view = new ModelEditView(getContext(), layout, delegate);
		return view;
	}
	
	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			showEditView(id);
			return true;
		}
	};
	
	private OnClickListener addNewListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			showEditView(Model.NONEXISTENT);
		}
	};
	
	private void showEditView(long id) {
		// TODO implement get(Class, long) in DatabaseHelper
		
		EditView edit = getEditView(id);
		
		NavigateBackView.Item item = new NavigateBackView.Item(getContext());
		item.opener = IndexView.this;
		item.view = edit;
		item.keep = false;
		
		flipper.showView(item);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (itemPickedListener != null) {
				// TODO implement get(Class, long) in DatabaseHelper
				itemPickedListener.onItemPicked(null);
			}
		}
	};
	
	private OnItemPickedListener<T> itemPickedListener;
	
	public void setOnItemPickedListener(OnItemPickedListener<T> listener) {
		itemPickedListener = listener;
	}
	
	public interface OnItemPickedListener<T> {
		void onItemPicked(T item);
	}
	
	private class ModelEditView extends EditView {

		public ModelEditView(Context ctx, int layout, Delegate helper) {
			super(ctx, layout, delegate);
		}
		
	}
}
