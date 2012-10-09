package com.zybnet.abc.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zybnet.abc.R;
import com.zybnet.abc.fragment.Compact;
import com.zybnet.abc.utils.FixturesDatabase;

public class AbbecedarioActivity extends FragmentActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        // TODO if layout is landscape, add two fragments: Table and Slot
        if (savedInstanceState == null) {
        	getSupportFragmentManager().beginTransaction().add(R.id.root, new Compact()).commit();
        }
        
        SQLiteDatabase db = new FixturesDatabase(this, null, null, 1).getWritableDatabase();
        db.execSQL("SELECT * FROM subject");
    }

}
