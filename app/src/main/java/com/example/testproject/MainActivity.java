package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private int height;
    private Handler handler;
    private ObjectAnimator animationDown2;
    private ObjectAnimator animationDown;
    private ObjectAnimator animationUp;
    private AnimatorSet animatorSet;
    private AnimatorSet animatorSetGeneral;
    private DisplayMetrics displayMetrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        handler = new Handler();
        animatorSetGeneral = new AnimatorSet();
        animatorSet = new AnimatorSet();

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stopAnimator();
                moveText(event.getX(), event.getY());
                textView.setTextColor(getResources().getColor(R.color.change_color));
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stA().start();
                    }
                }, 5000);
                return true;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAnimator();
            }
        });
    }

    private void moveText(float x, float y) {
        textView.setX(x - (textView.getWidth() >> 1));
        textView.setY(y - (textView.getHeight() >> 1));
    }

    private AnimatorSet stA() {

        animationDown2 = ObjectAnimator.ofFloat(textView,"translationY", textView.getTranslationY(), height / 2.2f);
        animationDown2.setDuration(3000);
        animationDown = ObjectAnimator.ofFloat(textView,"translationY", -(height / 2.2f), height / 2.2f);
        animationDown.setDuration(3000);
        animationUp = ObjectAnimator.ofFloat(textView,"translationY", height / 2.2f, -(height / 2.2f));
        animationUp.setDuration(3000);

        animatorSet.playSequentially(animationUp, animationDown);
        animatorSetGeneral.play(animationDown2).before(animatorSet);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });

        return animatorSetGeneral;

    }

    private void stopAnimator() {
        if (animationDown2 != null) {
            animationDown2.cancel();
        }
        if (animationDown != null) {
            animationDown.cancel();
        }
        if (animationUp != null) {
            animationUp.cancel();
        }
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        if (stA().isRunning()) {
            stA().removeAllListeners();
            stA().cancel();
        }
    }
}