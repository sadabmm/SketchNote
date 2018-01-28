package com.example.smm.SketchNote;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.smm.SketchNote.adapter.ViewPagerAdapter;
import com.example.smm.SketchNote.dialogfragment.EraseImageDialogFragment;
import com.example.smm.SketchNote.dialogfragment.LineWidthDialogFragment;
import com.example.smm.SketchNote.fragment.PaintFragment;
import com.example.smm.SketchNote.fragment.OCRFragment;
import com.example.smm.SketchNote.helper.DoodleFragment;
import com.example.smm.SketchNote.helper.LockableViewPager;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    LockableViewPager viewPager;

    //Floating Action Menu
    FloatingActionMenu fabMenu;
    FloatingActionButton fabButtonColor, fabButtonWidth, fabButtonDelete, fabButtonSave, fabButtonPrint;

    public static ViewPagerAdapter adapter;
    public PaintFragment paint;

    private int currentBackgroundColor = 0xffffffff; //initial color set to white

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);
        viewPager.setSwipeable(false); //Modified viewPager to disable swipe in the viewpager.

        tabLayout.setupWithViewPager(viewPager);

        setFragmentToViewPager(); //method to set the fragments in viewpager

        //FAB code
        fabMenu = findViewById(R.id.fab);
        fabButtonColor = findViewById(R.id.color);
        fabButtonWidth = findViewById(R.id.lineWidth);
        fabButtonDelete = findViewById(R.id.erase);
        fabButtonSave = findViewById(R.id.save);
        fabButtonPrint = findViewById(R.id.print);

        fabButtonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ColorDialogFragment fragment = new ColorDialogFragment();
                fragment.show(getSupportFragmentManager(), "color dialog");*/

                ColorPickerDialogBuilder
                        .with(MainActivity.this)
                        .setTitle("Choose Color")
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                Log.d("COLOR", String.valueOf(selectedColor));
                                DoodleFragment.getDoodleFragment().getDoodleView().setDrawingColor(selectedColor);
                                Toast.makeText(getApplicationContext(), "Selected Color: " + Integer.toHexString(selectedColor).toUpperCase(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();

                fabMenu.close(true);
            }
        });

        fabButtonWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineWidthDialogFragment fragment1 = new LineWidthDialogFragment();
                fragment1.show(getSupportFragmentManager(), "line width dialog");
                fabMenu.close(true);
            }
        });

        fabButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EraseImageDialogFragment fragment = new EraseImageDialogFragment();
                fragment.show(getSupportFragmentManager(), "erase paint");
                fabMenu.close(true);

                //Removing the text from textView
                DoodleFragment.getOCRFragment().OCRTextView.setText("");
            }
        });

        fabButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoodleFragment.getDoodleFragment().saveImage();
                fabMenu.close(true);
            }
        });

        fabButtonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoodleFragment.getDoodleFragment().getDoodleView().printImage();
                fabMenu.close(true);
            }
        });
    }

    private void setFragmentToViewPager() {
        paint = new PaintFragment();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addToFragment(paint, "Sketch Note");
        adapter.addToFragment(new OCRFragment(), "Convert to Text");
        viewPager.setAdapter(adapter);
    }
}