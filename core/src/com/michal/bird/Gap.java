package com.michal.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Gap extends Entity {
    //static float lastPosition = 600f; // czemu tu nie ma wskaznikow :'(
    static Gap lastCreated = new Gap(); // to sluzy jako wskaznik do ostatniego stworzonego obiektu
    private float getY(){
        Random randomNumberGenerator = new Random();
        return randomNumberGenerator.nextInt((int) (Gdx.graphics.getHeight() * 0.70));
    }
    public Gap(){
        try { // tyle roboty bo wskaznikow nie ma ja nie moge
            position = new Vector2(lastCreated.getPosition().x+400f, getY());
        } catch (Exception e) {
            position = new Vector2(1000f, getY());
        }
        texture = new Texture("pipe.png");
        velocity = new Vector2(-5.5f,0);
        try {
            lastCreated = this; // szczerze mowiac nie sadzilem ze to zadziala
        } catch (Exception e) { } // catch dziala bo wszystkie klasy dziedzicza z Exception
    }
    @Override
    public void draw(SpriteBatch batch){
        batch.draw(texture, position.x, position.y-800);
        batch.draw(texture, position.x, position.y+170);
    }
    public static void reset() {
        lastCreated = null;
    }
    protected void die(){
            position.x = lastCreated.position.x + 400f;
            position.y = getY();
            lastCreated = this;
    }

    @Override
    protected float topY() {
        return bottomY()+170f;
    }
}