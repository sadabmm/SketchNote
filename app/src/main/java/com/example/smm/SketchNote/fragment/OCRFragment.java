package com.example.smm.SketchNote.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smm.SketchNote.MainActivity;
import com.example.smm.SketchNote.R;
import com.example.smm.SketchNote.adapter.ViewPagerAdapter;
import com.example.smm.SketchNote.helper.DoodleFragment;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OCRFragment extends Fragment {

    Bitmap image;
    private TessBaseAPI mTess;
    String datapath;
    Button convertButton;
    public TextView OCRTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ocr, container, false);

        //Image of the sketch text
        convertButton = view.findViewById(R.id.convertToText);
        OCRTextView = view.findViewById(R.id.textViewOCR);

        datapath = getContext().getFilesDir() + "/tesseract/";

        //make sure training data has been copied
        checkFile(new File(datapath + "tessdata/"));

        //initialize Tesseract API
        String lang = "eng";
        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OCRresult;
                image = DoodleFragment.getDoodleFragment().getDoodleView().getBitmap();
                mTess.setImage(image);
                OCRresult = mTess.getUTF8Text();
                OCRTextView.setText(OCRresult);

                if(OCRresult.length() == 0){
                    Toast.makeText(getContext(), "NO WORDS FOUND", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(getContext());
        }
        //The directory exists, but there is no data file in it
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(getContext());
            }
        }
    }

    private void copyFiles(Context context) {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = context.getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("File Not Found", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IO Error", e.toString());
        }
    }
}