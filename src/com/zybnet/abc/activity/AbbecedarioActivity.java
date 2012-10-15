package com.zybnet.abc.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zybnet.abc.R;
import com.zybnet.abc.fragment.BaseFragment;
import com.zybnet.abc.fragment.CompactFragment;
import com.zybnet.abc.fragment.ExtendedFragment;
import com.zybnet.abc.utils.DatabaseHelper;
import com.zybnet.abc.utils.FixturesDatabaseHelper;
import com.zybnet.abc.utils.L;
import com.zybnet.abc.utils.SQLiteCursorLoader;
import com.zybnet.abc.view.NavigateBackView;
import com.zybnet.abc.view.SaveButtonView;
import com.zybnet.abc.view.TableView;

public class AbbecedarioActivity extends FragmentActivity {
	
	private final int LOADER_SLOTS = 1;
	
	public final String EXTENDED_FRAGMENT = "table";
	public final String COMPACT_FRAGMENT = "compact";
	
	private DatabaseHelper dbHelper;
	private SQLiteLoaderCallbacks callbacks;
	private PopupWindow popup;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        // Setup fragments
        if (findViewById(R.id.root) != null) {
        	addFragmentMaybe(CompactFragment.class, R.id.root, COMPACT_FRAGMENT);
        } else {
        	addFragmentMaybe(ExtendedFragment.class, R.id.right, EXTENDED_FRAGMENT);
        }
        
        setupActionBar();
        
        // TODO this is not persistent
        dbHelper = new FixturesDatabaseHelper(this);
        callbacks = new SQLiteLoaderCallbacks();
        
        getSupportLoaderManager().initLoader(LOADER_SLOTS, null, callbacks);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if (popup.isShowing()) {
    		popup.dismiss();
    	}
    }
    
    private class ClosePopupListener implements View.OnClickListener{
    	View.OnClickListener chain;
    	
    	ClosePopupListener(View.OnClickListener chain) {
    		this.chain = chain;
    	}
    	
    	@Override
    	public void onClick(View view) {
    		popup.dismiss();
    		chain.onClick(view);
    	}
    }
    
    private void setupActionBar() {
    	final View content = getLayoutInflater().
    			inflate(R.layout.menu, new LinearLayout(this, null), true);
    	content.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
    	
    	content.findViewById(R.id.settings).setOnClickListener(new ClosePopupListener(
    					getActiveFragment().getSettingsMenuClickedListener())
    	);
    	
    	popup = new PopupWindow(content, content.getMeasuredWidth(), content.getMeasuredHeight(), true);
    	popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
    	popup.setOutsideTouchable(true);
    	
    	popup.setAnimationStyle(R.style.AnimationPopup);
    	
    	findViewById(R.id.actionbar_menu).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View button) {
				popup.showAtLocation(button, Gravity.TOP | Gravity.RIGHT, 0, button.getHeight());
			}
		});
    	
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
    
    public BaseFragment getActiveFragment() {
    	FragmentManager fm = getSupportFragmentManager();
    	fm.executePendingTransactions();
    	return 	(BaseFragment) fm.findFragmentByTag(
    			(findViewById(R.id.root) == null) ? EXTENDED_FRAGMENT : COMPACT_FRAGMENT);
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
    
    public NavigateBackView getBackButton() {
    	return (NavigateBackView) findViewById(R.id.actionbar_back);
    }
    
    public SaveButtonView getSaveButton() {
    	return (SaveButtonView) findViewById(R.id.actionbar_save);
    }
    
    public TableView getTableView() {
    	return (TableView) findViewById(TableView.ID);
    }

    public DatabaseHelper db() {
    	return dbHelper;
    }
}
