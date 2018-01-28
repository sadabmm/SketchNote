package com.example.smm.SketchNote.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.example.smm.SketchNote.MainActivity;
import com.example.smm.SketchNote.R;
import com.example.smm.SketchNote.adapter.ViewPagerAdapter;
import com.example.smm.SketchNote.fragment.PaintFragment;
import com.example.smm.SketchNote.helper.DoodleFragment;

/**
 * Created by SMM on 27-Dec-17.
 */

public class EraseImageDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_erase);
        builder.setPositiveButton(R.string.button_erase, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DoodleFragment.getDoodleFragment().getDoodleView().clear();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
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
}
