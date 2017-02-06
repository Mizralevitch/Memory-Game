package com.example.michalizralevitch.memorygame;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by michalizralevitch on 08/05/16.
 */
public class EndActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    public static final int PREVIOUSLEVEL = 5;
    public static final int NEXTLEVEL = 6;
    public static final int QUIT = 7;
    private TextView pointsYouHaveSoFar;
    private Button btnCbPreviousLevel;
    private Button btnCbNextLevel;
    private Button btnCbQuit;
    private View v;
    View confeti1Cb, confeti2Cb;

    int viewWidth,viewHeight;

    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        mAdView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        pointsYouHaveSoFar = (TextView) findViewById(R.id.pointsYouHaveSoFar);
        btnCbPreviousLevel = (Button) findViewById(R.id.btnPreviousLevel);
        btnCbNextLevel = (Button) findViewById(R.id.btnNextLevel);
        btnCbQuit = (Button) findViewById(R.id.btnQuit);

        int score = getIntent().getIntExtra("SCORE", 0);
        pointsYouHaveSoFar.setText((score) + "");

        btnCbPreviousLevel.setOnClickListener(this);
        btnCbNextLevel.setOnClickListener(this);
        btnCbQuit.setOnClickListener(this);

        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.clapping);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Boolean streamMusic=getIntent().getExtras().getBoolean("MUTE?");
        if (streamMusic)
             mPlayer.start();


        //declaring the view where the animation will take place
        v = (ViewGroup)findViewById(R.id.activity_end);

        ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {

            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    else
                        v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    viewWidth = v.getWidth();
                    viewHeight = v.getHeight();
                    doAnimation();


                }

            });

        }

        }


    public void doAnimation() {

        confeti1Cb=(View)findViewById(R.id.confeti1);
        confeti1Cb.setMinimumWidth(viewWidth);
        confeti1Cb.setMinimumHeight(viewHeight);


        confeti2Cb=(View)findViewById(R.id.confeti2);
        confeti2Cb.setMinimumWidth(viewWidth);
        confeti2Cb.setMinimumHeight(viewHeight);


        ParticleSystem ps = new ParticleSystem(this,70,R.mipmap.ic_launcher2,10000);
        ps.setSpeedModuleAndAngleRange(0f, 0.3f, 0, 180);
        ps.setRotationSpeed(100);
        ps .setAcceleration(0.00005f, 90);
        ps.emit(confeti1Cb, 8);



        ParticleSystem ps1 = new ParticleSystem(this, 70,R.mipmap.ic_launcher, 10000);
        ps1.setSpeedModuleAndAngleRange(0f, 0.3f, 0, 180);
        ps1.setRotationSpeed(110);
        ps1.setAcceleration(0.00005f, 90);
        ps1.emit(confeti2Cb, 8);



        ParticleSystem ps2 = new ParticleSystem(this, 70,R.mipmap.ic_launcher1, 10000);
        ps2.setSpeedModuleAndAngleRange(0f, 0.3f, 0, 180);
        ps2.setRotationSpeed(110);
        ps2.setAcceleration(0.00005f, 90);
        ps2.emit(confeti1Cb, 8);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnNextLevel:
                setResult(NEXTLEVEL);
                finish();
                break;
            case R.id.btnPreviousLevel:
                setResult(PREVIOUSLEVEL);
                finish();
                break;
            case R.id.btnQuit:
                setResult(QUIT);
                finish();
                break;

        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
