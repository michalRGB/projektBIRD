package com.michal.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;

public class Entity implements Drawable, Disposable {
    protected Texture texture;
    protected Vector2 position;
    protected Vector2 velocity;

    protected boolean dead = false;

    /*protected Entity(Texture texture, Vector2 position, Vector2 velocity){
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
    }*/ // okazuje sie ze super() musi byc pierwsze w konstruktorze dziecka a niestety w gapie musze dostac pozycje wiec sie nie oplaca

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getEndX(){
        return position.x + texture.getWidth();
    }

    public boolean isDead(){
        return dead;
    }

    public int getWidth() {
        return texture.getWidth();
    }

    public void update(){
        position.y += velocity.y;
        position.x += velocity.x;
        checkDeath();
    }

    public void draw(SpriteBatch batch){
        if (getEndX()<0) //optymalizcja 101
            return;
        batch.draw(texture,position.x, position.y);
    }
    protected void checkDeath() {
        if (position.x + texture.getWidth()< 0 || position.y < 0)
            die();
    }
    protected void die(){
        if (position.x < 0)
            position.x = 0;
        if (position.y < 0)
            position.y = 0;
        dead = true;
        velocity.x = -5.5f;
        velocity.y = 0;
    }

    protected float topY() {
        return position.y+texture.getHeight();
    }
    protected float rightX(){
        return position.x+texture.getWidth();
    }
    protected float bottomY() {
        return position.y;
    }
    protected float leftX() {
        return position.x;
    }
    void checkIfCollidesAndDie(Entity entity) {
       if (rightX()<entity.leftX())
           return;
       if (leftX()>entity.rightX())
           return;
       if (topY()<entity.bottomY())
           return;
       if (bottomY()>entity.topY())
           return;
       die();
    }

    void checkIfCollidesAndDie(Gap gap) {
        if (rightX()<gap.leftX())
            return;
        if (leftX()>gap.rightX())
            return;
        if (topY()>=gap.topY() || bottomY()<=gap.bottomY())
            die();
    }
    public void dispose(){
        texture.dispose();
    }

}
