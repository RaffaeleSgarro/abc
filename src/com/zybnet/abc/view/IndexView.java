package com.zybnet.abc.view;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
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
import com.zybnet.abc.activity.AbbecedarioActivity;
import com.zybnet.abc.model.MessageBus;
import com.zybnet.abc.model.MessageBus.Action;
import com.zybnet.abc.model.Model;
import com.zybnet.abc.model.NotNull;
import com.zybnet.abc.model.Subscriber;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.IndexController;
import com.zybnet.abc.utils.U;

@SuppressLint("ViewConstructor")
public class IndexView<T extends Model> extends LinearLayout implements Subscriber<T> {

	private TextView title;
	private ListView list;
	private HistoryViewFlipper flipper;
	private Class<T> modelClassToken;
	private DatabaseHelper dh;
	private IndexController<T> controller;
	
	public IndexView(AbbecedarioActivity abc, Class<T> token, IndexController<T> controller) {
		super(abc);
		
		this.modelClassToken = token;
		this.dh = abc.db();
		this.controller = controller;
		
		setOrientation(LinearLayout.VERTICAL);
		
		U.setPaddingLeft(this, 10);
		U.setPaddingRight(this, 10);
		
		LayoutInflater inflater = abc.getLayoutInflater();
		inflater.inflate(R.layout.index, this, true);
		
		title = (TextView) findViewById(R.id.title);
		list = (ListView) findViewById(R.id.list);
		
		findViewById(R.id.add_new).setOnClickListener(addNewListener);
		
		controller.fillList(this);
	}
	
	@Override
	public void onAttachedToWindow() {
		MessageBus.subscribe(modelClassToken, this);
	}
	
	@Override
	public void onDetachedFromWindow() {
		MessageBus.unsuscribe(modelClassToken, this);
	}
	
	public void setFlipper(HistoryViewFlipper flipper) {
		this.flipper = flipper;
	}
	
	public void setTitle(String str) {
		title.setText(str);
	}
	
	public void setListAdapter(ListAdapter adapter) {
		list.setAdapter(adapter);
		list.setOnItemClickListener(itemClickListener);
		list.setOnItemLongClickListener(itemLongClickListener);
	}
	
	private EditView getEditView(Long id) {
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
			showEditView(null);
		}
	};
	
	private void showEditView(Long id) {
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
			super(IndexView.this.getContext(), layout, new ModelDelegate(model));
		}
		
	}
	
	public class ModelDelegate extends EditView.Delegate {
		
		protected T model;
		private Map<Field, TextView> bindings = new HashMap<Field, TextView>();
		
		public ModelDelegate(T model) {
			
			this.model = model;
		}
		
		@Override
		public void afterInflate(EditView view) {
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
				
				if (type.equals(String.class)) {
					textView.setText((String) value);
					
					if (field.isAnnotationPresent(NotNull.class)) {
						textView.setHint(R.string.required);
					}
					
				} else if (type.equals(java.sql.Date.class)) {
					java.sql.Date date = value != null ? (java.sql.Date) value : new java.sql.Date(System.currentTimeMillis());
					((DateEditText) textView).setDate(date);
				} else {
					throw new IllegalArgumentException(type.getCanonicalName());
				}
				
			}
			controller.fixAfterInflate(view, model);
		}
		
		@Override
		public void save(EditView view) {
			for (Map.Entry<Field, TextView> entry : bindings.entrySet()) {
				TextView textView = entry.getValue();
				CharSequence seq = textView.getText();
				Field field = entry.getKey();
				
				// Don't change empty fields
				if (seq.length() < 1) {
					if (field.isAnnotationPresent(NotNull.class)) {
						textView.requestFocus();
						return;
					} else {
						continue;
					}
				}
					
				
				Class<?> type = field.getType();
				Object value;
				
				if (type.equals(String.class)) {
					value = seq.toString();
				} else if (type.equals(java.sql.Date.class)) {
					try {
						java.util.Date date = DateFormat.getDateInstance().parse(seq.toString());
						java.sql.Date sqlDate = new java.sql.Date(date.getTime());
						value = sqlDate;
					} catch (ParseException e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new IllegalArgumentException(type.getCanonicalName());
				}
				
				try {
					field.set(model, value);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}	
			}
			controller.fixBelongsTo(model);
			controller.fixBeforeSave(view, model);
			model.save(dh);
			flipper.back();
		}
		
		@Override
		public void delete(EditView view) {
			flipper.back();
			model.delete(dh);
		}
	}

	@Override
	public void onMessage(T message, Action action) {
		controller.fillList(this);
	}
}
