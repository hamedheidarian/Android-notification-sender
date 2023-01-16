package com.example.screenprojectornavigation.ui.drawing;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.screenprojectornavigation.DrawView;
import com.example.screenprojectornavigation.R;
import com.example.screenprojectornavigation.logic.NetworkThread;
import com.google.android.material.slider.RangeSlider;

public class DrawingFragment extends Fragment {
    private DrawView paint;
    private ImageButton save, color, stroke, undo;

    private RangeSlider rangeSlider;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drawing, container, false);
        paint = (DrawView) root.findViewById(R.id.draw_view);
        rangeSlider = (RangeSlider) root.findViewById(R.id.rangebar);
        undo = (ImageButton) root.findViewById(R.id.btn_undo);
        save = (ImageButton) root.findViewById(R.id.btn_save);
        stroke = (ImageButton) root.findViewById(R.id.btn_stroke);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.undo();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bmp = paint.save();
                System.out.println("#####################");
                System.out.println(bmp);
                NetworkThread networkThread = NetworkThread.createUDPSenderForBitmap(bmp);
                networkThread.run();
                System.out.println("out of thread...");

//                OutputStream imageOutStream = null;
//                ContentValues cv = new ContentValues();
//                cv.put(MediaStore.Images.Media.DISPLAY_NAME,"drawing.png");
//                cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
//                //TODO send it via wifi to arduino board
//                cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
//                try {
//                    imageOutStream = getContentResolver().openOutputStream(uri);
//                    bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
//                    imageOutStream.close();
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rangeSlider.getVisibility() == View.VISIBLE)
                    rangeSlider.setVisibility(View.GONE);
                else
                    rangeSlider.setVisibility(View.VISIBLE);
            }
        });

        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                paint.setStrokeWidth((int) value);
            }
        });

        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);
            }
        });
        return root;
    }


}