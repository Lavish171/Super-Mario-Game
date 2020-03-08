package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class Mario extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
    Texture []man;
    Texture coin,bomb;
    int manstate=0,pause=0;
    int manY=0;
    float velocityY=0.0f;
    float gravity=0.5f;
    Random random;
    int coincount=0;
    int bombcount=0;
    Texture dizzy;
    ArrayList<Integer> coinsX=new ArrayList<>();
    ArrayList<Integer> coinsY=new ArrayList<>();
    ArrayList<Integer> bombX=new ArrayList<>();
	ArrayList<Integer> bombY=new ArrayList<>();
	ArrayList<Rectangle> coinsrectangle=new ArrayList<>();
	ArrayList<Rectangle> bombsrectangle=new ArrayList<>();
	Rectangle manRectangle;
	BitmapFont bitmapFont;
	int score=0,gamestate=0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man= new Texture[4];
		dizzy=new Texture("dizzy-1.png");
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		manY=Gdx.graphics.getHeight()/2;
		random=new Random();
		manRectangle=new Rectangle();
		bitmapFont=new BitmapFont();
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.getData().scale(9);
	}

	public void makecoin()
	{
		float height=random.nextFloat()*(float)(Gdx.graphics.getHeight());
		coinsY.add((int)height);
		coinsX.add(Gdx.graphics.getWidth());
	}

	public void makebomb()
	{
		float height=random.nextFloat()*(float)(Gdx.graphics.getHeight());
		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gamestate==0)
		{
			if(Gdx.input.justTouched())
			{
				gamestate=1;
			}
		}
		else if(gamestate==1)
		{

			if(coincount<50)
				coincount++;
			else
			{
				coincount=0;
				makecoin();
			}

			if(bombcount<200)
				bombcount++;
			else
			{
				bombcount=0;
				makebomb();
			}
			coinsrectangle.clear();
			for(int i=0;i<coinsX.size();i++)
			{
				batch.draw(coin,coinsX.get(i),coinsY.get(i));
				coinsX.set(i,coinsX.get(i)-20);
				coinsrectangle.add(new Rectangle(coinsX.get(i),coinsY.get(i),coin.getWidth(),coin.getHeight()));
			}


			for(int i=0;i<bombX.size();i++)
			{
				batch.draw(bomb,bombX.get(i),bombY.get(i));
				bombX.set(i,bombX.get(i)-12);
				bombsrectangle.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth()/4,bomb.getHeight()/2));
			}

			if(Gdx.input.justTouched())
				velocityY=-12;
			if(pause<10)
			{
				pause++;
			}
			else {
				pause = 0;
				if (manstate < 3) {
					manstate++;
				} else {
					manstate = 0;
				}
			}
			velocityY+=gravity;
			manY-=velocityY;
			if(manY<=0) {
				manY=0;
			}

		}//end of game live part
		else if(gamestate==2)
		{
			//Game Over
			if(Gdx.input.justTouched())
			{
				gamestate=1;
				score=0;
				manY=Gdx.graphics.getHeight()/2;
				velocityY=0;
				coinsX.clear();
				coinsY.clear();
				bombX.clear();
				bombY.clear();
				coinsrectangle.clear();
				bombsrectangle.clear();
				coincount=0;
				bombcount=0;
			}
		}
		/////\\\\//
		if(gamestate==2)
		{
		batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, manY);
		}
		else {
			batch.draw(man[manstate], Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, manY);
		}
		manRectangle=new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,man[manstate].getWidth(),man[manstate].getHeight());
		for(int i=0;i<coinsrectangle.size();i++)
		{
			if(Intersector.overlaps(manRectangle,coinsrectangle.get(i)))
			{
			Gdx.app.log("Coin ","Collision");
			score++;
			coinsrectangle.remove(i);
			coinsX.remove(i);
			coinsY.remove(i);
			break;
			}
		}

		for(int i=0;i<bombsrectangle.size();i++)
		{
			if(Intersector.overlaps(manRectangle,bombsrectangle.get(i)))
			{
				Gdx.app.log("Bomb ","Collision");
				gamestate=2;
			}
		}
		bitmapFont.draw(batch,String.valueOf(score),100,200);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
