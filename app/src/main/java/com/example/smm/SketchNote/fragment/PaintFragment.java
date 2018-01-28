package com.example.smm.SketchNote.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smm.SketchNote.R;
import com.example.smm.SketchNote.dialogfragment.EraseImageDialogFragment;
import com.example.smm.SketchNote.view.DoodleView;

public class PaintFragment extends Fragment {

    private DoodleView doodleView; // handles touch events and draws
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean dialogOnScreen = false;

    // value used to determine whether user shook the device to erase
    private static final int ACCELERATION_THRESHOLD = 100000;

    // used to identify the request for using external storage, which
    // the save image feature needs
    public static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_doodle, container, false);

        setHasOptionsMenu(false); // this fragment has menu items to display
        doodleView = view.findViewById(R.id.doodleView);

        acceleration = 0.00f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableAccelerometerListening();
    }

    private void enableAccelerometerListening() {
        // get the SensorManager
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        // register to listen for accelerometer events
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        disableAccelerometerListening();
    }

    private void disableAccelerometerListening() {
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!dialogOnScreen) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                lastAcceleration = currentAcceleration;
                currentAcceleration = x * x + y * y + z * z;

                acceleration = currentAcceleration * (currentAcceleration - lastAcceleration);

                if (acceleration > ACCELERATION_THRESHOLD) {
                    confirmErase();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void confirmErase() {
        EraseImageDialogFragment fragment = new EraseImageDialogFragment();
        fragment.show(getFragmentManager(), "erase dialog");
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.doodle_fragment_menu, menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.color:
                ColorDialogFragment fragment = new ColorDialogFragment();
                fragment.show(getFragmentManager(), "color dialog");
                return true;
            case R.id.line_width:
                LineWidthDialogFragment fragment1 = new LineWidthDialogFragment();
                fragment1.show(getFragmentManager(), "line width dialog");
                return true;
            case R.id.delete_drawing:
                confirmErase();
                return true;
            case R.id.save:
                saveImage();
                return true;
            case R.id.print:
                doodleView.printImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void saveImage() {
        if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage(R.string.permission_explanation);
                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_IMAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                dialog.create().show();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_IMAGE_PERMISSION_REQUEST_CODE);
            }
        } else {
            doodleView.saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SAVE_IMAGE_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doodleView.saveImage();
                }
                return;
        }
    }

    public DoodleView getDoodleView() {
        return doodleView;
    }

    public void setDialogOnScreen(boolean visible) {
        dialogOnScreen = visible;
    }
}
