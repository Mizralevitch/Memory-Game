package com.example.michalizralevitch.memorygame;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Memory game with 3 stages

    private Toolbar toolbar;

    private static final int GAME_OVER_REQUEST_CODE=1;
    private int NUM_ROWS;
    private int NUM_COLUMNS;
    public LinearLayout linearLayout;
    int counter;

    int[]numbers;

    private static int scoreSoFar=0;
    private int scoreTemporarly;

    boolean streamMusic=true;

     int[]images={R.drawable.monster1,R.drawable.monster2,R.drawable.monster3,R.drawable.monster4
             ,R.drawable.monster5,R.drawable.monster6,R.drawable.monster7,R.drawable.monster8,
             R.drawable.monster9,R.drawable.monster10,R.drawable.monster11,R.drawable.monster12,
                R.drawable.monster13,R.drawable.monster14,R.drawable.monster15};

    private int turns;
    private ImageView previousCard;
    int rightGuesses =0;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        startNewGame(4, 3, 0);

    }

    private void startNewGame(int numRows, int numColoums, int score){

        if (numRows==7){
            this.NUM_ROWS=6;
            this.NUM_COLUMNS=5;
        }else {
            this.NUM_ROWS = numRows;
            this.NUM_COLUMNS = numColoums;
        }
        numbers=new int[(NUM_COLUMNS*NUM_ROWS)];

        scoreTemporarly=0;
        scoreSoFar=score;

        buildBoard();
        setUpNumbersArray();

        counter=0;
        rightGuesses=0;
        turns=0;

        for (int i = 0; i <numbers.length ; i++) {
            previousCard=(ImageView)linearLayout.findViewWithTag(i);
            previousCard.setImageResource(R.drawable.card1);
            previousCard.setOnClickListener(this);
        }

        shuffle();
    }

    private void buildBoard() {

        if (linearLayout!=null)
            linearLayout.removeAllViews();

        linearLayout=(LinearLayout)findViewById(R.id.main_layout);

        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight=1;
        for (int i = 0; i <NUM_ROWS ; i++) {
            LinearLayout rowLayout=new LinearLayout(this);
            for (int j = 0; j <NUM_COLUMNS ; j++) {

                ImageView imageView = new ImageView(this);
                imageView.setTag(counter++);
                imageView.setOnClickListener(this);
                imageView.setImageResource(R.drawable.card1);
                imageView.setPadding(5,3,5,3);
                rowLayout.addView(imageView,params);

            }
            linearLayout.addView(rowLayout,params);
        }

    }


    private void setUpNumbersArray() {
        for (int i = 0; i <numbers.length ; i++) {
            numbers[i]=(i/2);
        }

    }

    private void shuffle(){

        int temp=0;
        Random random=new Random();
        for (int i = 0; i <numbers.length ; i++) {
            int r=random.nextInt(numbers.length);
            temp=numbers[i];
            numbers[i]=numbers[r];
            numbers[r]=temp;

        }
    }

    @Override
    public void onClick(View v) {

        ImageView clickCard = (ImageView) v;
        int image = images[numbers[(int) v.getTag()]];
        clickCard.setImageResource(image);
        if (clickCard != previousCard) {
            if (turns % 2 != 0) {
                if (checkCards(clickCard, previousCard)) {
                    clickCard.setOnClickListener(null);
                    previousCard.setOnClickListener(null);
                    MediaPlayer mPlayer = MediaPlayer.create(this,R.raw.tada);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    if (streamMusic)
                        mPlayer.start();
                    rightGuesses++;

                } else {
                    WrongThread thread = new WrongThread(clickCard, previousCard);
                    thread.start();
                }
            } else
                previousCard = clickCard;
            turns++;
            if (rightGuesses==(NUM_COLUMNS*NUM_ROWS)/2){
                scoreTemporarly=(NUM_COLUMNS*NUM_ROWS*3-turns);
                if (scoreTemporarly<0)
                    scoreTemporarly=0;
                scoreSoFar+=scoreTemporarly;

                Intent intent= new Intent(this,EndActivity.class);
                intent.putExtra("SCORE", scoreSoFar);
                intent.putExtra("MUTE?",streamMusic);
                startActivityForResult(intent,GAME_OVER_REQUEST_CODE);
            }
        }
    }
    private boolean checkCards(ImageView clickedCard, ImageView previousCard){
        return numbers[(int)previousCard.getTag()]==numbers[(int)clickedCard.getTag()];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==GAME_OVER_REQUEST_CODE){

            switch (resultCode){
                case EndActivity.PREVIOUSLEVEL:
                    startNewGame(NUM_ROWS,NUM_COLUMNS,scoreSoFar-scoreTemporarly);
                    break;
                case EndActivity.NEXTLEVEL:
                    startNewGame(NUM_ROWS+1,NUM_COLUMNS+1,scoreSoFar);
                    break;
                case EndActivity.QUIT:
                    finish();
            }


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

    public void muteOrUnmute(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (streamMusic) {
                    view.setBackgroundResource(R.drawable.mute_sound);
                    streamMusic = false;
                }else {
                    view.setBackgroundResource(R.drawable.unmute_sounds);
                    streamMusic=true;
                }
            }
        });

    }
}
