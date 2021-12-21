package com.example.flappybird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrPipes;
    private int sumpipe, distance;
    private int score, bestscore=0;
    private boolean start;
    private Context context;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null){
            bestscore = sp.getInt("bestscore", 0);
        }
        score = 0;
        start = false;
        initBird();
        initPipe();
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    private void initPipe() {
        sumpipe = 6;
        distance = 300*Constant.SCREEN_HEIGHT/1920;
        arrPipes = new ArrayList<>();
        for(int i = 0; i < sumpipe; i++){
            if(i < sumpipe/2){
                this.arrPipes.add(new Pipe(Constant.SCREEN_WIDTH+i*((Constant.SCREEN_WIDTH+200*Constant.SCREEN_WIDTH/1080)/(sumpipe/2)),
                        0, 200*Constant.SCREEN_WIDTH/1080, Constant.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
                this.arrPipes.get(this.arrPipes.size()-1).randomY();
            }else{
                this.arrPipes.add(new Pipe(this.arrPipes.get(i-sumpipe/2).getX(), this.arrPipes.get(i-sumpipe/2).getY()
                +this.arrPipes.get(i-sumpipe/2).getHeight() + this.distance,
                        200*Constant.SCREEN_WIDTH/1080, Constant.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe1));
            }
        }
    }

    private void initBird(){
        bird = new Bird();
        bird.setWidth(100*Constant.SCREEN_WIDTH/1080);
        bird.setHeight(100*Constant.SCREEN_HEIGHT/1920);
        bird.setX(100*Constant.SCREEN_WIDTH/1080);
        bird.setY(Constant.SCREEN_HEIGHT/2-bird.getHeight()/2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird2));
        bird.setArrBms(arrBms);
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        if(start){
            bird.draw(canvas);
            for(int i = 0; i < sumpipe; i++){
                if(bird.getRect().intersect(arrPipes.get(i).getRect())||
                bird.getY()-bird.getHeight() < 0 ||
                bird.getY() > Constant.SCREEN_HEIGHT){
                    Pipe.speed = 0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("Best: "+bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                    MainActivity.rl_game_over.setVisibility(VISIBLE);
                }
                if(this.bird.getX()+this.bird.getWidth() > arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2
                        &&this.bird.getX()+this.bird.getWidth() <= arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2+Pipe.speed
                        &&i<sumpipe/2){
                    score++;
                    if(score > bestscore){
                        bestscore = score;
                        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("Bestscore", bestscore);
                        editor.apply();
                    }
                    MainActivity.txt_score.setText(""+score);
                }
                if(this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth()){
                    this.arrPipes.get(i).setX(Constant.SCREEN_WIDTH);
                    if(i < sumpipe/2){
                        arrPipes.get(i).randomY();
                    }else{
                        arrPipes.get(i).setY(this.arrPipes.get(i-sumpipe/2).getY()
                                +this.arrPipes.get(i-sumpipe/2).getHeight() + this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }
        }else{
            if(bird.getY() > Constant.SCREEN_HEIGHT/2){
                bird.setDrop(-15*Constant.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }
        handler.postDelayed(r, 10);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.txt_score.setText("0");
        score = 0;
        initPipe();
        initBird();
    }
}
