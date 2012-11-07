package com.zybnet.abc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import com.zybnet.abc.utils.L;
import com.zybnet.abc.view.NavigateBackView;
import com.zybnet.abc.view.TableView;

public class AbbecedarioActivity extends FragmentActivity {
	
	public final String EXTENDED_FRAGMENT = "table";
	public final String COMPACT_FRAGMENT = "compact";
	
	private DatabaseHelper dbHelper;
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
        
        TableView table = new TableView(this);
		getActiveFragment().addTable(table);
        
        dbHelper = new DatabaseHelper(this);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if (popup.isShowing()) {
    		popup.dismiss();
    	}
    }
    
    /*
     * First thing, close the popup when an item is clicked.
     * Then invoked the proxied listener
     */
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
    	
    	content.findViewById(R.id.reset).setOnClickListener(new ClosePopupListener(
    			new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						reset();
					}
				})
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
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();
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
    
    public NavigateBackView getBackButton() {
    	return (NavigateBackView) findViewById(R.id.actionbar_back);
    }
    
    public TableView getTableView() {
    	return (TableView) findViewById(TableView.ID);
    }

    public DatabaseHelper db() {
    	return dbHelper;
    }
    
    private void reset() {
    	db().close();
    	this.deleteDatabase(DatabaseHelper.FILENAME);
    	finish();
    	startActivity(getIntent());
    }
}
