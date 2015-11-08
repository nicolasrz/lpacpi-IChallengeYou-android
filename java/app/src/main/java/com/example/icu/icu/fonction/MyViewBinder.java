package com.example.icu.icu.fonction;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

/**
 * Created by Nicolas on 29/03/2015.
 */public class MyViewBinder implements SimpleAdapter.ViewBinder {



    @Override

    public boolean setViewValue(View view, Object data,

                                String textRepresentation) {



        if( (view instanceof ImageView) & (data instanceof Bitmap) ) {

            ImageView iv = (ImageView) view;

            Bitmap bm = (Bitmap) data;

            iv.setImageBitmap(bm);

            return true;

        }



        return false;

    }

}