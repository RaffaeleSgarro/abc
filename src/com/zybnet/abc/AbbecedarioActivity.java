package com.zybnet.abc;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zybnet.abc.fragment.TimeTable;

public class AbbecedarioActivity extends FragmentActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO if layout is landscape, add two fragments
        setContentView(R.layout.main);
        getSupportFragmentManager().beginTransaction().add(R.id.root, new TimeTable()).commit();
    }

}
