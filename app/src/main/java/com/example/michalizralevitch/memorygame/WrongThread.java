package com.example.michalizralevitch.memorygame;

import android.widget.ImageView;

import android.os.Handler;

/**
 * Created by michalizralevitch on 08/05/16.
 */
public class WrongThread extends Thread {

    private ImageView clickedCard;
    private ImageView previousCard;
    private Handler handler;
    private int delayTime=1000;

    public WrongThread(ImageView clickedCard, ImageView previousCard){
        this.clickedCard=clickedCard;
        this.previousCard=previousCard;
        handler=new Handler();
    }

    @Override
    public void run() {
        try {
            sleep(delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                clickedCard.setImageResource(R.drawable.card1);
                previousCard.setImageResource(R.drawable.card1);
            }
        });

    }
}
