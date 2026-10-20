package com.michal.bird;

import java.io.Serializable;

public class MutableNeuralNetwork implements Serializable {
    MutableMatrix itohWeights;
    MutableMatrix htooWeights;
    MutableMatrix itohBias;
    MutableMatrix htooBias;
    public MutableNeuralNetwork(int input,int hidden, int output){
        itohWeights = new MutableMatrix(hidden,input);
        htooWeights = new MutableMatrix(output,hidden);
        itohBias = new MutableMatrix(hidden,1);
        htooBias = new MutableMatrix(output,1);
    }

    public double feedforward(double[] input){
        MutableMatrix hidden = MutableMatrix.multiply(itohWeights,new MutableMatrix(input));
        hidden.add(itohBias);
        hidden.sigmoidify();
        MutableMatrix output = MutableMatrix.multiply(htooWeights,hidden);
        output.add(htooBias);
        output.sigmoidify();
        return output.totalSum();
    }

    public void mutate(){
        //System.out.println();
        //itohWeights.print();
        itohWeights.mutate();
        htooWeights.mutate();
        itohBias.mutate();
        htooBias.mutate();
        //System.out.println();
        //itohWeights.print();
    }

    public MutableNeuralNetwork copy(){
        MutableNeuralNetwork temp = new MutableNeuralNetwork(itohWeights.columns(),itohWeights.rows(), htooWeights.rows());
        temp.itohWeights = itohWeights.copy();
        temp.htooWeights = htooWeights.copy();
        temp.itohBias = itohBias.copy();
        temp.htooBias = htooBias.copy();
        return temp;
    }
    void print(){
        itohWeights.print();
        htooWeights.print();
        itohBias.print();
        htooBias.print();
    }
}
