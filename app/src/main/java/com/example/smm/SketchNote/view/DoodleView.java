package com.example.smm.SketchNote.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import com.example.smm.SketchNote.R;

import java.util.Map;

public class DoodleView extends View {

    private static final float TOUCH_TOLERANCE = 10;

    private Bitmap bitmap; // drawing area for displaying or saving
    private Canvas bitmapCanvas; // used to to draw on the bitmap
    private final Paint paintScreen; // used to draw bitmap onto screen
    private final Paint paintLine; // used to draw lines onto bitmap

    // Maps of current Paths being drawn and Points in those Paths
    private final Map<Integer, Path> pathMap = new HashMap<>();
    private final Map<Integer, Point> previousPointMap = new HashMap<>();

    public DoodleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintScreen = new Paint();
        paintLine = new Paint();

        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(10);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.STROKE);

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH){
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    // clear the painting
    public void clear() {
        pathMap.clear(); // remove all paths
        previousPointMap.clear(); // remove all previous points
        bitmap.eraseColor(Color.WHITE); // clear the bitmap
        invalidate(); // refresh the screen
    }

    public void setDrawingColor(int color){
        paintLine.setColor(color);
    }

    public int getDrawingColor(){
        return paintLine.getColor();
    }

    // set the painted line's width
    public void setLineWidth(int width) {
        paintLine.setStrokeWidth(width);
    }

    // return the painted line's width
    public int getLineWidth() {
        return (int) paintLine.getStrokeWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);

        for(Integer key: pathMap.keySet()){
            canvas.drawPath(pathMap.get(key), paintLine);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();

        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN){
            touchStarted(event.getX(actionIndex), event.getY(actionIndex), event.getPointerId(actionIndex));
        } else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP){
            touchEnded(event.getPointerId(actionIndex));
        } else {
            touchMoved(event);
        }
        invalidate(); // redraw
        return true;
    }

    private void touchStarted(float x, float y, int pointerId) {
        Path path; // used to store the path for the given touch id
        Point point; // used to store the last point in path

        if (pathMap.containsKey(pointerId)) {
            path = pathMap.get(pointerId); // get the Path
            path.reset(); // resets the Path because a new touch has started
            point = previousPointMap.get(pointerId); // get Path's last point
        }
        else {
            path = new Path();
            pathMap.put(pointerId, path); // add the Path to Map
            point = new Point(); // create a new Point
            previousPointMap.put(pointerId, point); // add the Point to the Map
        }

        // move to the coordinates of the touch
        path.moveTo(x, y);
        point.x = (int) x;
        point.y = (int) y;
    }

    private void touchMoved(MotionEvent event) {
        // for each of the pointers in the given MotionEvent
        for (int i = 0; i < event.getPointerCount(); i++) {
            // get the pointer ID and pointer index
            int pointerID = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerID);

            // if there is a path associated with the pointer
            if (pathMap.containsKey(pointerID)) {
                // get the new coordinates for the pointer
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);

                // get the path and previous point associated with
                // this pointer
                Path path = pathMap.get(pointerID);
                Point point = previousPointMap.get(pointerID);

                // calculate how far the user moved from the last update
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                // if the distance is significant enough to matter
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    // move the path to the new location
                    path.quadTo(point.x, point.y, (newX + point.x) / 2,(newY + point.y) / 2);

                    // store the new coordinates
                    point.x = (int) newX;
                    point.y = (int) newY;
                }
            }
        }
    }

    private void touchEnded(int pointerId) {
        Path path = pathMap.get(pointerId); // get the corresponding Path
        bitmapCanvas.drawPath(path, paintLine); // draw to bitmapCanvas
        path.reset(); // reset the Path
    }

    public void saveImage(){
        final String name = "Doodlz" + System.currentTimeMillis() + ".jpg";
        String location = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, name, "Doodlz Drawing!");

        if(location != null){
            Toast message = Toast.makeText(getContext(), R.string.message_saved, Toast.LENGTH_LONG);
            message.setGravity(Gravity.CENTER, message.getXOffset() / 2,message.getYOffset() / 2);
            message.show();
        } else {
            // display a message indicating that there was an error saving
            Toast message = Toast.makeText(getContext(), R.string.message_error_saving, Toast.LENGTH_SHORT);
            message.setGravity(Gravity.CENTER, message.getXOffset() / 2, message.getYOffset() / 2);
            message.show();
        }
    }

    public void printImage(){
        if (PrintHelper.systemSupportsPrint()) {
            // use Android Support Library's PrintHelper to print image
            PrintHelper printHelper = new PrintHelper(getContext());

            // fit image in page bounds and print the image
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            printHelper.printBitmap("Doodlz Image", bitmap);
        }
        else {
            // display message indicating that system does not allow printing
            Toast message = Toast.makeText(getContext(), R.string.message_error_printing, Toast.LENGTH_SHORT);
            message.setGravity(Gravity.CENTER, message.getXOffset() / 2, message.getYOffset() / 2);
            message.show();
        }
    }
}