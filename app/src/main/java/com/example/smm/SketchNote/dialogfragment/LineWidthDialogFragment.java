package com.example.smm.SketchNote.dialogfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.smm.SketchNote.MainActivity;
import com.example.smm.SketchNote.R;
import com.example.smm.SketchNote.adapter.ViewPagerAdapter;
import com.example.smm.SketchNote.fragment.PaintFragment;
import com.example.smm.SketchNote.helper.DoodleFragment;
import com.example.smm.SketchNote.view.DoodleView;

/**
 * Created by SMM on 27-Dec-17.
 */

public class LineWidthDialogFragment extends DialogFragment {
    private ImageView widthImageView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View lineWidthDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_line_width, null);
        builder.setView(lineWidthDialogView); // add GUI to dialog

        // set the AlertDialog's message
        builder.setTitle(R.string.title_line_width_dialog);

        // get the ImageView
        widthImageView = lineWidthDialogView.findViewById(R.id.widthImageView);

        final DoodleView doodleView = DoodleFragment.getDoodleFragment().getDoodleView();
        final SeekBar widthSeekBar = lineWidthDialogView.findViewById(R.id.widthSeekBar);
        widthSeekBar.setOnSeekBarChangeListener(lineWidthChanged);
        widthSeekBar.setProgress(doodleView.getLineWidth());

        builder.setPositiveButton(R.string.button_set_line_width, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doodleView.setLineWidth(widthSeekBar.getProgress());
                    }
                }
        );
        return builder.create(); // return dialog
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        PaintFragment fragment = DoodleFragment.getDoodleFragment();

        if (fragment != null) {
            fragment.setDialogOnScreen(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        PaintFragment fragment = DoodleFragment.getDoodleFragment();

        if (fragment != null) {
            fragment.setDialogOnScreen(false);
        }
    }

    private final SeekBar.OnSeekBarChangeListener lineWidthChanged = new SeekBar.OnSeekBarChangeListener() {
        final Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Paint p = new Paint();
            p.setColor(DoodleFragment.getDoodleFragment().getDoodleView().getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);

            // erase the bitmap and redraw the line
            bitmap.eraseColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}