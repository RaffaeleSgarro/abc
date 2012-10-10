package com.zybnet.abc.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.zybnet.abc.R;
import com.zybnet.abc.fragment.Compact;
import com.zybnet.abc.utils.Database;
import com.zybnet.abc.utils.FixturesDatabase;
import com.zybnet.abc.utils.SQLiteCursorLoader;
import com.zybnet.abc.view.TimeTable;

public class AbbecedarioActivity extends FragmentActivity {
	
	private final int LOADER_SLOTS = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        // TODO if layout is landscape, add two fragments: Table and Slot
        if (savedInstanceState == null) {
        	getSupportFragmentManager().beginTransaction().add(R.id.root, new Compact()).commit();
        }
        
        getSupportLoaderManager().initLoader(LOADER_SLOTS, null, new CursorLoaderCallbacks());
    }
    
    public class CursorLoaderCallbacks implements LoaderCallbacks<Cursor> {

    	@Override
    	public Loader<Cursor> onCreateLoader(int requestId, Bundle params) {
    		Database helper = new FixturesDatabase(AbbecedarioActivity.this, null, null, 1);
    		SQLiteCursorLoader loader = new SQLiteCursorLoader(
    				AbbecedarioActivity.this, helper,
    				"SELECT slot._id, row, column, name_short FROM slot JOIN subject on slot.subject_id = subject._id", null);
    		return loader;
    	}

    	@Override
    	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    		TimeTable table = (TimeTable) findViewById(TimeTable.ID);
    		table.setCursor(cursor);
    	}

    	@Override
    	public void onLoaderReset(Loader<Cursor> loader) {
    		// TODO
    	}

    }

}
