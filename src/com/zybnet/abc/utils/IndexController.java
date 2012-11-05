package com.zybnet.abc.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;

import com.zybnet.abc.model.Model;
import com.zybnet.abc.view.EditView;
import com.zybnet.abc.view.IndexView;

public abstract class IndexController<T extends Model> {

	protected Context ctx;
	protected DatabaseHelper dh;
	
	public IndexController(Context ctx, DatabaseHelper dh) {
		this.ctx = ctx;
		this.dh = dh;
	}
	
	public void fixBeforeSave(EditView view, T model) {}

	public void fixAfterInflate(EditView view, T model) {}
	
	public void fillList(final IndexView<T> index) {
		new SetCursor(index).execute();
	}
	
	private class SetCursor extends AsyncTask<Void, Cursor, Cursor> {
		
		private IndexView<T> index;
		
		SetCursor(IndexView<T> index) {
			this.index = index;
		}
		
		public void onPostExecute(Cursor cursor) {
			CursorAdapter adapter = getAdapter(cursor);
			index.setListAdapter(adapter);
		}

		@Override
		protected Cursor doInBackground(Void... params) {
			return getCursor();
		}
	}
	
	protected abstract CursorAdapter getAdapter(Cursor cursor);
	
	protected abstract Cursor getCursor();
	
	public void fixBelongsTo(T model) {}
}
