package com.michal.bird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import java.io.*;
import java.util.Random;

public class BirdGame extends ApplicationAdapter {
	Bird[] birds;
	int birdPopulationSize = 500;
	Gap[] gaps;
	Gap nextGap;
	int currentIndex;
	Text scoreBoard;
	int generation;
	Text generationDisplay;
	Text howManyAliveDisplay;
	int highScore;
	Text highScoreDisplay;
	SpriteBatch batch;
	int allTimeHighScore = 0;
	Text allTimeHighScoreDisplay;

	// funkcje do startowania
	@Override
	public void create () {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("highScore.txt"));
			allTimeHighScore = Integer.parseInt(reader.readLine());
			reader.close();
		} catch (Exception e){
			allTimeHighScore = 0;

		}
		generation = 0;
		highScore = 0;
		birds = new Bird[birdPopulationSize];
		for (int i = 0;i<birds.length;++i)
			birds[i] = new Bird();
		batch = new SpriteBatch();
		createBirdless();
	}
	void createBirdless() {
		generation += 1;
		gaps = new Gap[3];
		for (int i=0; i<3; ++i)
			gaps[i] = new Gap();
		currentIndex = 0;
		nextGap = gaps[0];
		scoreBoard = new Text();
		generationDisplay = new Text();
		howManyAliveDisplay = new Text();
		highScoreDisplay = new Text();
		allTimeHighScoreDisplay = new Text();
	}
	//funkcje do aktualizowania
	private void update(){
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
			restart();
			MutableNeuralNetwork loadedNeuralNetwork = new MutableNeuralNetwork(1,1,1);
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serialized_neural_network.ser"))) {
				loadedNeuralNetwork = (MutableNeuralNetwork) ois.readObject();
			} catch (Exception e) {
			}
			Bird bird= new Bird();
			bird.setBrain(loadedNeuralNetwork);
			for (int i = 1;i<birds.length;++i)
				birds[i].die();
			birds[0] = bird;
		}
		updateList(birds);
		updateList(gaps);
		if (grantPoints())
			nextGap = getNexGap();

		checkCollisions(birds);

		if (howManyAlive(birds) < 1)
			restart();
		scoreBoard.set("punkty: " + getPoints(birds));
		generationDisplay.set("generacja: " + generation);
		howManyAliveDisplay.set("zywych: " + howManyAlive(birds));
		if (getPoints(birds)> highScore)
			highScore = getPoints(birds);
		highScoreDisplay.set("obecny rekord: " + highScore);
		allTimeHighScoreDisplay.set("rekord globalny: " + allTimeHighScore);
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			killall();


	}
	public void updateList(Entity [] entities) {
		for (Entity entity : entities)
			entity.update();
	}
	public void updateList(Bird [] birds){
		for (Bird bird : birds) {
			bird.update(nextGap.leftX(), nextGap.bottomY());
		}
	}
	private int getPoints(Bird [] birds){ // zwaraca jaki jest teraz wynik
		for (Bird bird : birds)
			if (!bird.isDead())
				return bird.getPoints();
		return 0; // chyba tak nigdy nie bedzie
	}
	private boolean grantPoints(){ // daje punkty ptakom jak wszystkie sa martwe zwraca false
		boolean granted = false;
		for (Bird bird : birds){
			if (nextGap.rightX() < bird.leftX()) {
				bird.grantPoint();
				granted = true;
			}
		}
		return granted;
	}
	Gap getNexGap(){
		if (currentIndex == gaps.length-1){
			currentIndex = 0;
			return gaps[currentIndex];
		}else
			return gaps[++currentIndex];
	}
	private void checkCollisions(Bird [] birds){
		for (Bird bird : birds)
			bird.checkIfCollidesAndDie(nextGap);
	}
	private int howManyAlive(Entity [] entities){
		int counter = 0;
		for (Entity entity : entities)
			if (!entity.isDead())
					counter += 1;
		return counter;
	}
	// funkcje restartowania
	private void killall(){
		for (Bird bird : birds)
			bird.die();
	}
	private void restart(){
		if (highScore > allTimeHighScore) {
			allTimeHighScore = highScore;
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("highScore.txt"));
				writer.write(allTimeHighScore+"\n");
				writer.close();
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serialized_neural_network.ser"));
				oos.writeObject(bestBird().brain());
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		repopulate();
		for (Gap gap : gaps) gap.dispose();
		gaps = null;
		Gap.reset();
		createBirdless();
		System.gc();
	}
	void repopulate(){
		double sum = 0;
		for (Bird bird : birds)
			sum += bird.getFitness();
		double[] normalizedFitnesses = new double[birds.length];
		for (int i = 0; i<normalizedFitnesses.length;++i)
			normalizedFitnesses[i] = birds[i].getFitness()/sum;
		Bird [] newBirds = new Bird[birdPopulationSize];
		newBirds[newBirds.length-1] = bestBird();
		for (int i = 0; i<newBirds.length-1;++i)
			newBirds[i] = birds[pickedIndex(normalizedFitnesses)].evolved();
		for (int i = 0; i<newBirds.length;++i)
			birds[i].dispose();
		birds = newBirds;
	}
	Bird bestBird(){
		int maxFitnessIndex = 0; // ta funkca jest napisana tak idiotycznie ze szkoda gadac ale to po to zeby nie robic disposa
		double maxFitness = 0;
		for (int i = 0;i<birds.length;++i)
			if (birds[i].getFitness() > maxFitness) {
				maxFitnessIndex = i;
				maxFitness = birds[i].getFitness();
			}
		Bird maxBird = new Bird();
		maxBird = birds[maxFitnessIndex].copy();
		maxBird.setBestTexture();
		return maxBird;
	}
	public int pickedIndex(double[] normalizedFitnesses){
		double sum=0;
		double randomNumber = new Random().nextDouble();
		for (int i = 0; i< normalizedFitnesses.length;++i) {
			sum += normalizedFitnesses[i];
			if (sum > randomNumber)
				return i;
		}
		return normalizedFitnesses.length -1;
	}
	// funkcje renderowania
	public void drawEverything(){
		clear();
		batch.begin();
		draw(birds);
		draw(gaps);
		draw(scoreBoard);
		generationDisplay.draw(100,100,batch);
		howManyAliveDisplay.draw((int)(Gdx.graphics.getWidth()*0.8), 100,batch);
		highScoreDisplay.draw((int)(Gdx.graphics.getWidth()*0.7), (int)(Gdx.graphics.getHeight()*0.95),batch);
		allTimeHighScoreDisplay.draw((int)(Gdx.graphics.getWidth()*0.4), (int)(Gdx.graphics.getHeight()*0.95),batch);;
		batch.end();
	}
	private void clear(){
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void draw(Drawable [] drawables){
		for (Drawable drawable : drawables)
			drawable.draw(batch);
	}
	private void draw(Drawable drawable){
		drawable.draw(batch);
	}
	// glowna funkcja
	@Override
	public void render () {
		if (getPoints(birds)<2)
			//for (int i = 0;i<100;++i)
				update(); // wiki poleca robic updata w tej funkcji ciekawe co jesli chcemy miec fixed fizyke
		else {
			update();
		}
		drawEverything();
	}
}