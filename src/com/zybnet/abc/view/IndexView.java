package com.zybnet.abc.view;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

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
		
		flipper.showView(item);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (itemPickedListener != null) {
				T model = dh.fill(modelClassToken, id);
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
		private Map<Field, TextView> bindings = new HashMap<Field, TextView>();
		
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
				// ID can't be displayed nor modified
				// neither for this model nor for related ones
				if (field.getName().endsWith("_id"))
					continue;
				
				Class<?> type = field.getType();
				int id = getResources().getIdentifier(field.getName(), "id", getContext().getPackageName());
				TextView textView = (TextView) view.findViewById(id);
				
				// Setup special text views...
				if (textView instanceof DateEditText) {
					((DateEditText) textView).setup(flipper);
				}
				
				bindings.put(field, textView);
				
				Object value;
				
				try {
					value = field.get(model);
				} catch (Exception e) {
					throw new RuntimeException(String.format("Can't get %s.%s %s",
							model.getClass().getSimpleName(),
							field.getName(),
							type.getCanonicalName(), e)
					);
				}
				
				if (value == null)
					continue;
				
				if (type.equals(String.class)) {
					textView.setText((String) value);
				} else if (type.equals(java.sql.Date.class)) {
					((DateEditText) textView).setDate((java.sql.Date) value);
				} else {
					throw new IllegalArgumentException(type.getCanonicalName());
				}
				
			}
		}
		
		@Override
		public void save(EditView view) {
			for (Map.Entry<Field, TextView> entry : bindings.entrySet()) {
				CharSequence seq = entry.getValue().getText();
				
				// Don't change empty fields
				if (seq.length() < 1)
					continue;
				
				Field field = entry.getKey();
				Class<?> type = field.getType();
				if (type.equals(String.class)) {
					L.og(String.format("Trying to set %s to %s",
							field.getName(), seq.toString()));
				} else if (type.equals(java.sql.Date.class)) {
					try {
						java.util.Date date = DateFormat.getDateInstance().parse(seq.toString());
						java.sql.Date sqlDate = new java.sql.Date(date.getTime());
						L.og(String.format("Saving %s to a date: %tF", field.getName(), sqlDate));
					} catch (ParseException e) {
						throw new RuntimeException(e);
					}
				}
			}
			chained.save(view);
		}
		
		@Override
		public void delete(EditView view) {
			chained.delete(view);
		}
	}
}
