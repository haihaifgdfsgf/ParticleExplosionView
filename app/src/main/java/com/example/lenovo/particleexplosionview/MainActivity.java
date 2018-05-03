package com.example.lenovo.particleexplosionview;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private View mView;
    private ParticleView mParticleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParticleView = new ParticleView(this);
        mView = findViewById(R.id.view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("mytag", "click");
                mParticleView.startBoom(view, new ParticleView.OnAnimationListener() {
                    @Override
                    public void onAnimationStart(View v, Animator animation) {
                        Log.i("mytag", "startanimate");
                        mView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(View v, Animator animation) {
                        mView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
