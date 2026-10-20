package com.michal.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Bird extends Entity implements Disposable {
    private int points;
    //static Texture ramSaving = new Texture("bird.png");
    private MutableNeuralNetwork brain;
    int ticksAlive = 0;
    public Bird(){
        texture =  new Texture("bird.png");
        position = new Vector2(200, Gdx.graphics.getHeight() / 2f - texture.getHeight() / 2f);
        velocity = new Vector2(0,0);
        brain = new MutableNeuralNetwork(4,4,1);
    }
    public Bird evolved(){
        Bird bird = this.copy();
        bird.brain.mutate();
        return bird;
    }
    boolean toJumpOrNotToJump(double[] inputs){
        return brain.feedforward(inputs) > 0.5;
    }
    public void update(float nextPipeLeftX, float nextPipeBottomY){
        if (!dead) {
            ++ticksAlive;
//            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
//                velocity.y += 18;
            double[] array = {nextPipeLeftX/Gdx.graphics.getWidth(),nextPipeBottomY/Gdx.graphics.getHeight(), position.y/Gdx.graphics.getHeight(), velocity.y/-7f};
            if (toJumpOrNotToJump(array))
                velocity.y += 18;
            velocity.y -= 1;
            if (velocity.y < -7f)
                velocity.y = -7f;
            else if (velocity.y > 18)
                velocity.y = 18;

            position.y += velocity.y;
            if (position.y > 600)
                position.y = 600;

            checkDeath();
        } else {
            position.x += velocity.x;
        }
    }
    public int getFitness(){
        return ticksAlive*ticksAlive;
    }
    public void grantPoint(){
        ++points;
    }

    public int getPoints(){
        return points;
    }
    public Bird copy(){
        Bird temp = new Bird();
        temp.brain = this.brain.copy();
        //ticksAlive = 0;
        //dead = false;
        return temp;
    }
    public void setBestTexture(){
        texture.dispose();
        texture = new Texture("bird1.png");
    }
    void print(){
        brain.print();
    }
    public MutableNeuralNetwork brain(){
        return brain;
    }
    public void setBrain(MutableNeuralNetwork newBrain){
        this.brain = newBrain;
    }
}
