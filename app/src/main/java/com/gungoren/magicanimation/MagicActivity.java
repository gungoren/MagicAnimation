package com.gungoren.magicanimation;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.gungoren.view.MagicAnimationCompleteListener;
import com.gungoren.view.MagicImageView;

/**
 * Created by mehmetgungoren on 14.12.2018.
 */

public class MagicActivity extends AppCompatActivity {

    MagicImageView magicImageView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        magicImageView = findViewById(R.id.outerView);
        imageView = findViewById(R.id.innerView);

        imageView.setOnTouchListener(new MagicViewTouchListener(magicImageView, imageView, new MagicAnimationCompleteListener() {
            @Override
            public void onComplete(Bitmap bitmap) {
                imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
            }
        }));
    }
}
