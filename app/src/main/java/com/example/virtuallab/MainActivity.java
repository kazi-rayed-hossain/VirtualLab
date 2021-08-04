package com.example.virtuallab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;
    ImageView image;
    TextView logo, slogan;
    Animation topanim,bottomanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView3);
        slogan = findViewById(R.id.textView2);

        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image.setAnimation(topanim);
        logo.setAnimation(bottomanim);
        slogan.setAnimation(bottomanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}
