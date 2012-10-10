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
import com.zybnet.abc.fragment.Compact;
import com.zybnet.abc.fragment.Slot;
import com.zybnet.abc.fragment.Table;
import com.zybnet.abc.utils.Database;
import com.zybnet.abc.utils.FixturesDatabase;
import com.zybnet.abc.utils.L;
import com.zybnet.abc.utils.SQLiteCursorLoader;
import com.zybnet.abc.view.TimeTable;

public class AbbecedarioActivity extends FragmentActivity {
	
	private final int LOADER_SLOTS = 1;
	private final String TABLE_FRAGMENT = "table";
	private final String COMPACT_FRAGMENT = "compact";
	private final String SLOT_FRAGMENT = "slot";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        if (findViewById(R.id.root) != null) {
        	addFragmentMaybe(Compact.class, R.id.root, COMPACT_FRAGMENT);
        } else {
        	addFragmentMaybe(Table.class, R.id.right, TABLE_FRAGMENT);
        	addFragmentMaybe(Slot.class, R.id.left, SLOT_FRAGMENT);
        }
        
        getSupportLoaderManager().initLoader(LOADER_SLOTS, null, new CursorLoaderCallbacks());
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
    
    private Fragment getTableFragment() {
    	return 	getSupportFragmentManager().findFragmentByTag(
    			(findViewById(R.id.root) == null) ? TABLE_FRAGMENT : COMPACT_FRAGMENT);
    }
    
    public class CursorLoaderCallbacks implements LoaderCallbacks<Cursor> {

    	@Override
    	public Loader<Cursor> onCreateLoader(int requestId, Bundle params) {
    		Database helper = new FixturesDatabase(AbbecedarioActivity.this, null, null, 1);
    		SQLiteCursorLoader loader = new SQLiteCursorLoader(
    				AbbecedarioActivity.this, helper,
    				"SELECT slot._id, slot.day, slot.ord, slot.subject_id," +
    				"  subject.name_short AS subject_name_short" +
    				" FROM slot JOIN subject on slot.subject_id = subject._id", null);
    		return loader;
    	}

    	@Override
    	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    		Fragment tableFragment = getTableFragment();
    		TimeTable table = (TimeTable) tableFragment.getView().findViewById(TimeTable.ID);
    		if (!cursor.isBeforeFirst())
    			cursor.moveToPosition(-1);
    		table.setCursor(cursor);
    	}

    	@Override
    	public void onLoaderReset(Loader<Cursor> loader) {
    		// TODO
    	}

    }

}
