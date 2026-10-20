package com.michal.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;

public class Text implements Drawable{
    String text;
    BitmapFont font = new BitmapFont();
    public void draw(int x, int y,SpriteBatch batch){
        font.getData().setScale(2);
        font.draw(batch, text, x, y);
    }

    public void set(String text){
        this.text = text;
    }

    public void set(int number){
        set(Integer.toString(number));
    }

    public void draw(SpriteBatch batch){
        //draw(Gdx.graphics.getWidth()/2, (int) (Gdx.graphics.getHeight()*0.95),batch);
        draw(100, (int) (Gdx.graphics.getHeight()*0.95),batch);
    }
    public void dispose(){
        font.dispose();
    }
}
