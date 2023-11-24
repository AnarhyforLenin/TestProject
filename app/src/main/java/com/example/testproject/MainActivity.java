package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
    private ObjectAnimator animatorDownFromObjectState;
    private ObjectAnimator animatorDown;
    private ObjectAnimator animatorUp;
    private AnimatorSet animatorSetUpAndDown;
    private AnimatorSet animatorSetGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        handler = new Handler();
        animatorSetGeneral = new AnimatorSet();
        animatorSetUpAndDown = new AnimatorSet();

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stopAllAnimations();
                moveText(event.getX(), event.getY());
                textView.setTextColor(getResources().getColor(R.color.change_color));
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllAnimations().start();
                    }
                }, 5000);
                return true;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAllAnimations();
            }
        });
    }

    private void moveText(float x, float y) {
        textView.setX(x - (textView.getWidth() >> 1));
        textView.setY(y - (textView.getHeight() >> 1));
    }

    private AnimatorSet getAllAnimations() {

        animatorDownFromObjectState = ObjectAnimator.ofFloat(textView,"translationY", textView.getTranslationY(), height / 2.2f);
        animatorDownFromObjectState.setDuration(3000);
        animatorDown = ObjectAnimator.ofFloat(textView,"translationY", -(height / 2.2f), height / 2.2f);
        animatorDown.setDuration(3000);
        animatorUp = ObjectAnimator.ofFloat(textView,"translationY", height / 2.2f, -(height / 2.2f));
        animatorUp.setDuration(3000);

        animatorSetUpAndDown.playSequentially(animatorUp, animatorDown);
        animatorSetGeneral.play(animatorDownFromObjectState).before(animatorSetUpAndDown);

        animatorSetUpAndDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetUpAndDown.start();
            }
        });

        return animatorSetGeneral;

    }

    private void stopAllAnimations() {
        if (animatorDownFromObjectState != null) {
            animatorDownFromObjectState.cancel();
        }
        if (animatorDown != null) {
            animatorDown.cancel();
        }
        if (animatorUp != null) {
            animatorUp.cancel();
        }
        if (animatorSetUpAndDown != null) {
            animatorSetUpAndDown.removeAllListeners();
            animatorSetUpAndDown.cancel();
        }
        if (getAllAnimations().isRunning()) {
            getAllAnimations().removeAllListeners();
            getAllAnimations().cancel();
        }
    }
}