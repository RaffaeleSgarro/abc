package com.zybnet.abc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.zybnet.abc.fragments.Exams;
import com.zybnet.abc.fragments.TimeTable;

public class AbbecedarioActivity extends FragmentActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapterImpl(getSupportFragmentManager()));
    }
    
    private class PagerAdapterImpl extends FragmentStatePagerAdapter {

		public PagerAdapterImpl(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public Fragment getItem(int index) {
			switch (index) {
			case 0: return new TimeTable(6, 6); // TODO configurable
			case 1: return new Exams();
			default: throw new RuntimeException("No such index " + index);
			}
		}
    	
    }
}
