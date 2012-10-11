package com.zybnet.abc.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;

import com.zybnet.abc.R;
import com.zybnet.abc.fragment.CompactFragment;
import com.zybnet.abc.fragment.LeftFragment;
import com.zybnet.abc.fragment.RightFragment;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.FixturesDatabaseHelper;
import com.zybnet.abc.utils.L;
import com.zybnet.abc.utils.SQLiteCursorLoader;
import com.zybnet.abc.view.TableView;

public class AbbecedarioActivity extends FragmentActivity {
	
	private final int LOADER_SLOTS = 1;
	
	private final String TABLE_FRAGMENT = "table";
	private final String COMPACT_FRAGMENT = "compact";
	private final String SLOT_FRAGMENT = "slot";
	
	private DatabaseHelper dbHelper;
	private SQLiteLoaderCallbacks callbacks;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        if (findViewById(R.id.root) != null) {
        	addFragmentMaybe(CompactFragment.class, R.id.root, COMPACT_FRAGMENT);
        } else {
        	addFragmentMaybe(RightFragment.class, R.id.right, TABLE_FRAGMENT);
        	addFragmentMaybe(LeftFragment.class, R.id.left, SLOT_FRAGMENT);
        }
        
        // TODO this is not persistent
        dbHelper = new FixturesDatabaseHelper(this);
        callbacks = new SQLiteLoaderCallbacks();
        
        getSupportLoaderManager().initLoader(LOADER_SLOTS, null, callbacks);
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	if (getSupportLoaderManager().hasRunningLoaders()) {
    		callbacks.setPendingCloseHelper();
    	} else {
    		dbHelper.close();
    	}
    }
    
    /*
     * Adds the Fragment to the container with the specified ID
     * if none with the same TAG is found
     */
    private void addFragmentMaybe(Class<? extends Fragment> cf, int id, String tag) {
    	FragmentManager fm = getSupportFragmentManager();
    	Fragment f = fm.findFragmentByTag(tag);
    	if (f == null) {
    		try {
				f = cf.getConstructor().newInstance();
				fm.beginTransaction().add(id, f, tag).commit();
			} catch (Exception e) {
				Log.e(L.TAG, "Cannot instantiate fragment " + cf.getCanonicalName());
				throw new RuntimeException(e);
			}
    	}
    }
    
    public Fragment getTableFragment() {
    	return 	getSupportFragmentManager().findFragmentByTag(
    			(findViewById(R.id.root) == null) ? TABLE_FRAGMENT : COMPACT_FRAGMENT);
    }
    
    private class SQLiteLoaderCallbacks implements LoaderCallbacks<Cursor> {

    	private boolean pendingCloseHelper = false;
    	
    	public void setPendingCloseHelper() {
    		pendingCloseHelper = true;
    	}
    	
    	@Override
    	public Loader<Cursor> onCreateLoader(int requestId, Bundle params) {
    		DatabaseHelper helper = new FixturesDatabaseHelper(AbbecedarioActivity.this, null, null, 1);
    		SQLiteCursorLoader loader = new SQLiteCursorLoader(
    				AbbecedarioActivity.this, helper,
    				"SELECT slot._id, slot.day, slot.ord, slot.subject_id," +
    				"  subject.name_short AS subject_name_short" +
    				" FROM slot JOIN subject on slot.subject_id = subject._id", null);
    		return loader;
    	}

    	@Override
    	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    		// Close everything and return
    		if (pendingCloseHelper) {
    			cursor.close();
    			dbHelper.close();
    			return;
    		}
    		
    		TableView table = getTableView();
    		if (!cursor.isBeforeFirst())
    			cursor.moveToPosition(-1);
    		table.setCursor(cursor);
    	}

    	@Override
    	public void onLoaderReset(Loader<Cursor> loader) {
    		// TODO
    	}

    }
    
    public TableView getTableView() {
    	return (TableView) getTableFragment().getView().findViewById(TableView.ID);
    }

    public DatabaseHelper db() {
    	return dbHelper;
    }
}
