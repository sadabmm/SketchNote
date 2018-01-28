package com.example.smm.SketchNote.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;

import com.example.smm.SketchNote.MainActivity;
import com.example.smm.SketchNote.R;
import com.example.smm.SketchNote.adapter.ViewPagerAdapter;
import com.example.smm.SketchNote.fragment.PaintFragment;
import com.example.smm.SketchNote.view.DoodleView;

/**
 * Created by SMM on 27-Dec-17.
 */

public class ColorDialogFragment extends DialogFragment {
    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private View colorView;
    private int color;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_color, null);

        builder.setView(colorDialogView);
        builder.setTitle(R.string.title_color_dialog);

        // get the color SeekBars and set their onChange listeners
        alphaSeekBar = colorDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar = colorDialogView.findViewById(R.id.redSeekBar);
        greenSeekBar = colorDialogView.findViewById(R.id.greenSeekBar);
        blueSeekBar = colorDialogView.findViewById(R.id.blueSeekBar);
        colorView = colorDialogView.findViewById(R.id.colorView);

        alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);

        // use current drawing color to set SeekBar values
        final DoodleView doodleView = getDoodleFragment().getDoodleView();

        color = doodleView.getDrawingColor();
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        greenSeekBar.setProgress(Color.green(color));
        blueSeekBar.setProgress(Color.blue(color));

        builder.setPositiveButton(R.string.button_set_color, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doodleView.setDrawingColor(color);
            }
        });
        return builder.create(); // return dialog
    }

    // gets a reference to the PaintFragment
    private PaintFragment getDoodleFragment() {
        //ViewPager mPager = new ViewPager(getActivity());
        //Log.i("POSITION", String.valueOf(mPager.getCurrentItem()));

        ViewPagerAdapter adapter = MainActivity.adapter;
        Fragment page = adapter.getRegisteredFragment(0);
        return (PaintFragment) page;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        PaintFragment fragment = getDoodleFragment();

        if(fragment != null){
            fragment.setDialogOnScreen(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        PaintFragment fragment = getDoodleFragment();

        if(fragment != null){
            fragment.setDialogOnScreen(false);
        }
    }

    private final SeekBar.OnSeekBarChangeListener colorChangedListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // user, not program, changed SeekBar progress
            if (fromUser) {
                color = Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(), greenSeekBar.getProgress(), blueSeekBar.getProgress());
                colorView.setBackgroundColor(color);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
