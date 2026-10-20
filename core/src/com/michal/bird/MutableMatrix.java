package com.michal.bird;
import java.io.Serializable;
import java.util.Random;

public class MutableMatrix implements Serializable {
    double[][] matrix;
    public MutableMatrix(int rows,int columns){
        if (rows < 1 || columns < 1)
            throw new RuntimeException("The size of matrix must be at lest 1x1");
        Random randomNumberGenerator = new Random();
        matrix = new double[rows][columns];
        for (int i=0;i<rows;++i)
            for (int j=0;j<columns;++j)
                matrix[i][j] = randomNumberGenerator.nextDouble(2d)-1d;;
    }

    public MutableMatrix(double[] array){
        matrix = new double[array.length][1];
        for (int i = 0; i < array.length;++i)
            matrix[i][0] = array[i];
    }
    public void nullify(){
        for (int i = 0; i<rows(); ++i)
            for (int j = 0; j< columns(); ++j)
                matrix[i][j] = 0;
    }
    public MutableMatrix copy(){
        MutableMatrix temp = new MutableMatrix(rows(), columns());
        for (int i = 0;i<rows();++i)
            for (int j = 0; j< columns(); ++j)
                temp.matrix[i][j] = matrix[i][j];
        return temp;
    }
    public int rows(){
        return matrix.length;
    }

    public int columns(){
        return matrix[0].length;
    }
    public void add(MutableMatrix a) { // czemu java nie ma overladingu operatorow
        for (int i=0;i<a.rows();++i)
            for (int j = 0; j<a.columns(); j++)
                matrix[i][j] += a.matrix[i][j];
    }
    void mutate(){
        Random randomNumberGenerator = new Random();
        for (int i=0;i<this.rows();++i)
            for (int j = 0; j<this.columns(); ++j)
                if (randomNumberGenerator.nextDouble() < 0.12) {
                    matrix[i][j] = randomNumberGenerator.nextDouble(2d) - 1d;
                }
    }
    void substract(MutableMatrix a){
        MutableMatrix temp = a.copy();
        temp.scalarMulitply(-1);
        add(temp);
    }
    public void sigmoidify(){
        for (int i = 0; i < rows(); ++i){
            for (int j = 0; j< columns(); ++j)
                matrix[i][j] = sigmoid(matrix[i][j]);
        }
    }
    public void print(){
        for (int i = 0; i < rows(); ++i){
            for (int j = 0; j< columns(); ++j)
                System.out.print(matrix[i][j] + " ");
            System.out.println();
        }
        System.out.flush();
    }
    static private double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
    }

    public double totalSum() {
        double sum = 0d;
        for (int i = 0; i<rows(); ++i)
            for (int j = 0; j< columns(); ++j)
                sum += matrix[i][j];
        return sum;
    }
    public void scalarMulitply(double scalar){
        for (int i = 0; i<this.rows(); ++i)
            for (int j = 0; j<this.columns(); ++j)
                matrix[i][j] *= scalar;
    }

    static public MutableMatrix multiply(MutableMatrix a, MutableMatrix b){
        if (a.columns() != b.rows())
                throw new RuntimeException("the number of rows of the first matrix must be equal to the number of columns of the second matrix");
        MutableMatrix resultantMatrix = new MutableMatrix(a.rows(),b.columns());
        resultantMatrix.nullify();
        for (int i = 0; i<resultantMatrix.rows(); ++i)
            for (int j = 0; j<resultantMatrix.columns(); ++j)
                for (int k = 0; k<a.columns(); ++k)
                    resultantMatrix.matrix[i][j] += a.matrix[i][k] * b.matrix[k][j];
        return resultantMatrix;
    }
}
