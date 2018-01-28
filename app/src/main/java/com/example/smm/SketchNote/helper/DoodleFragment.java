package com.example.smm.SketchNote.helper;

import android.support.v4.app.Fragment;

import com.example.smm.SketchNote.MainActivity;
import com.example.smm.SketchNote.adapter.ViewPagerAdapter;
import com.example.smm.SketchNote.fragment.OCRFragment;
import com.example.smm.SketchNote.fragment.PaintFragment;

/**
 * Created by SMM on 02-Jan-18.
 */

public class DoodleFragment {

    public static PaintFragment getDoodleFragment(){
        //ViewPager mPager = new ViewPager(getActivity());
        //Log.i("POSITION", String.valueOf(mPager.getCurrentItem()));

        ViewPagerAdapter adapter = MainActivity.adapter;
        Fragment page = adapter.getRegisteredFragment(0);
        return (PaintFragment) page;
    }

    public static OCRFragment getOCRFragment(){
        ViewPagerAdapter adapter = MainActivity.adapter;
        Fragment page = adapter.getRegisteredFragment(1);
        return (OCRFragment) page;
    }
}
