package com.zybnet.abc.view;

import java.lang.reflect.Field;

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
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.L;
import com.zybnet.abc.utils.U;

@SuppressLint("ViewConstructor")
public class IndexView<T extends Model> extends LinearLayout {

	private TextView title;
	private ListView list;
	private HistoryViewFlipper flipper;
	private Class<T> modelClassToken;
	private EditView.Delegate delegate;
	private DatabaseHelper dh;
	
	public IndexView(DatabaseHelper dh, HistoryViewFlipper flipper, Class<T> token, EditView.Delegate delegate) {
		super(flipper.getContext());
		
		this.flipper = flipper;
		this.modelClassToken = token;
		this.delegate = delegate;
		this.dh = dh;
		
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
		T model = dh.fill(modelClassToken, id);
		int layout = getResources().getIdentifier(
				"edit_" + modelClassToken.getSimpleName().toLowerCase(),
				"layout",
				getContext().getPackageName());
		EditView view = new ModelEditView(model, layout);
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
				T model = dh.fill(modelClassToken, id);
				L.og("Picked model:");
				L.og(model.dump());
				itemPickedListener.onItemPicked(model);
			}
		}
	};
	
	private OnItemPickedListener<T> itemPickedListener;
	
	public void setOnItemPickedListener(OnItemPickedListener<T> pickerListener) {
		itemPickedListener = pickerListener;
	}
	
	public interface OnItemPickedListener<T> {
		void onItemPicked(T item);
	}
	
	private class ModelEditView extends EditView {

		public ModelEditView(T model, int layout) {
			super(IndexView.this.getContext(), layout, new ModelDelegate(model, delegate));
		}
		
	}
	
	public class ModelDelegate extends EditView.Delegate {
		
		private EditView.Delegate chained;
		protected T model;
		
		public ModelDelegate(T model) {
			this(model, null);
		}
		
		public ModelDelegate(T model, EditView.Delegate chained) {
			if (chained == null) {
				chained = new EditView.Delegate();
			}
			
			this.chained = chained;
			this.model = model;
		}
		
		@Override
		public void afterInflate(EditView view) {
			chained.afterInflate(view);
			for (Field field : model.getPublicFields()) {
				Class<?> type = field.getType();
				if (type.equals(String.class)) {
					int id = getResources().getIdentifier(field.getName(), "id", getContext().getPackageName());
					try {
						((TextView) view.findViewById(id)).setText((String)field.get(model));
					} catch (Exception e) {
						throw new RuntimeException(String.format("Can't get %s.%s %s",
								model.getClass().getSimpleName(),
								field.getName(),
								type.getCanonicalName())
						);
					}
				}
			}
		}
		
		@Override
		public void save(EditView view) {
			chained.save(view);
		}
		
		@Override
		public void delete(EditView view) {
			chained.delete(view);
		}
	}
}
