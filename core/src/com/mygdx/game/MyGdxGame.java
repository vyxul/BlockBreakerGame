package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.actors.Ball;
import com.mygdx.game.actors.Block;
import com.mygdx.game.actors.Paddle;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
public class MyGdxGame extends ApplicationAdapter {
	ShapeRenderer ballShape;
	ShapeRenderer paddleShape;
	ShapeRenderer blockShape;
	ShapeRenderer backShape;
	ShapeRenderer frontShape;
	SpriteBatch batch;
	BitmapFont font;
	Ball ball;
	Paddle paddle;
	Block backBlock;
	Block frontBlock;
	ArrayList<Block> blocks = new ArrayList<>();
	MyInputProcessor myInputProcessor;
	Sound sound1;
	Sound sound2;
	int minStartXSpeed, maxStartXSpeed, initXSpeed, initYSpeed;
	private State state = State.PAUSE;
	int rowsMax = 5, columnsMax = 9;
	int blockCount = 0;
	int gameStartFlag = 1;
	int gameWonFlag = 0;
	int gameLostFlag = 0;

	@Override
	public void create () {
		/*
		 * On app startup
		 */
		myInputProcessor = new MyInputProcessor(this);
		Gdx.input.setInputProcessor(myInputProcessor);
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		ballShape = new ShapeRenderer();
		paddleShape = new ShapeRenderer();
		blockShape = new ShapeRenderer();
		backShape = new ShapeRenderer();
		frontShape = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		System.out.println("Screen dimensions (W x H): " + screenWidth + " x " + screenHeight);

		// Set up ball
		minStartXSpeed = -5;
		maxStartXSpeed = 5;
		initXSpeed = ThreadLocalRandom.current().nextInt(minStartXSpeed, maxStartXSpeed + 1);
		initYSpeed = 8;
		ball = new Ball((screenWidth / 2), (screenHeight / 5), 30, 0, 0);
		ball.setColor(Color.GRAY);
		System.out.println(ball.toString());

		// Set up paddle
		paddle = new Paddle((screenWidth / 2) - 50, 20, 100, 10);
		paddle.setColor(Color.GRAY);
		System.out.println(paddle.toString());

		// Set up blocks
		rowsMax = 5;
		columnsMax = 9;
		blockCount = rowsMax * columnsMax;
		int blockWidth = (screenWidth - 100) / columnsMax;
		int blockHeight = (screenHeight / 2) / rowsMax;
		for (int y = ((screenHeight - 10) - blockHeight), row = 0; row < rowsMax; y -= (blockHeight + 10), row++) {
			for (int x = 10, column = 0; column < columnsMax; x += (blockWidth + 10), column++) {
				Block block = new Block(x, y, blockWidth, blockHeight);
				block.setColor(Color.GRAY);
				blocks.add(block);
			}
		}

		// Set up the starting notification block
		// Two large blocks, the bigger one white, and smaller one white
		backBlock = new Block(screenWidth/4, screenHeight/4, screenWidth/2, screenHeight/2);
		frontBlock = new Block((screenWidth/4)+50, (screenHeight/4)+50, (screenWidth/2)-100, (screenHeight/2)-100 );

		sound1 = Gdx.audio.newSound(Gdx.files.internal("Balloon Pop 1.mp3"));
		sound2 = Gdx.audio.newSound(Gdx.files.internal("mixkit-electronic-retro-block-hit-2185.mp3"));

		long id = sound2.play(1.0f);
		System.out.println("sound2 id: " + id);
	}

	public void reset() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		this.gameStartFlag = 0;
		this.gameLostFlag = 0;
		this.gameWonFlag = 0;
		blockCount = rowsMax * columnsMax;

		// Set up ball
		this.minStartXSpeed = -5;
		this.maxStartXSpeed = 5;
		this.initXSpeed = ThreadLocalRandom.current().nextInt(minStartXSpeed, maxStartXSpeed + 1);
		this.initYSpeed = 8;
		this.ball.setColor(Color.GRAY);
		this.ball.setXSpeed(initXSpeed);
		this.ball.setYSpeed(initYSpeed);
		this.ball.setX((screenWidth / 2));
		this.ball.setY((screenHeight / 5));
		this.ball.setHitBottom(false);
		this.ball.setColor(Color.WHITE);

		// Set up paddle
		this.paddle.setX((screenWidth / 2) - 50);
		this.paddle.setY(20);
		this.paddle.setColor(Color.WHITE);

		// Set up blocks
		this.blocks.clear();
		int blockWidth = (screenWidth - 100) / columnsMax;
		int blockHeight = (screenHeight / 2) / rowsMax;
		for (int y = ((screenHeight - 10) - blockHeight), row = 0; row < rowsMax; y -= (blockHeight + 10), row++) {
			for (int x = 10, column = 0; column < columnsMax; x += (blockWidth + 10), column++) {
				Block block = new Block(x, y, blockWidth, blockHeight);
				block.setColor(Color.WHITE);
				this.blocks.add(block);
			}
		}
	}

	@Override
	public void render () {
		/*
		 * On every frame of the game, default is 60 FPS
		 */

		boolean pauseBtnPressed;
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		// While game state is RUN
		switch (state) {
			case PAUSE:
				/* This is an example of using polling to check for input, currently using event handling instead inside the MyInputProcessor.java class
				pauseBtnPressed = Gdx.input.isKeyJustPressed(Input.Keys.P);
				if (pauseBtnPressed) {
					System.out.println("Using polling to check to unpause game");
					this.setState(State.RUN);
				}
				 */

				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				ballShape.begin(ShapeRenderer.ShapeType.Filled);
				paddleShape.begin(ShapeRenderer.ShapeType.Filled);
				blockShape.begin(ShapeRenderer.ShapeType.Filled);
				backShape.begin(ShapeRenderer.ShapeType.Filled);
				frontShape.begin(ShapeRenderer.ShapeType.Filled);

				if (backBlock.isActive() && frontBlock.isActive()) {
					backBlock.setColor(Color.WHITE);
					frontBlock.setColor(Color.BLACK);
					backBlock.draw(backShape);
					frontBlock.draw(frontShape);
				}

				if (gameStartFlag == 1) {
					ball.setColor(Color.GRAY);
					paddle.setColor(Color.GRAY);

				}

				ball.draw(ballShape);
				paddle.draw(paddleShape);

				for (Block block : blocks) {
					block.setColor(Color.GRAY);
					block.draw(blockShape);
				}

				if (ball.getXSpeed() == 0 && ball.getYSpeed() == 0) {
					if (Gdx.input.justTouched()) {
						ball.setColor(Color.WHITE);
						paddle.setColor(Color.WHITE);
						for (Block block : blocks) {
							block.setColor(Color.WHITE);
						}

						this.menuSwitch(false);
						this.gameStartFlag = 0;
						this.setState(State.RUN);

						ball.setXSpeed(initXSpeed);
						ball.setYSpeed(initYSpeed);

						long id = sound1.play(1.0f);
						sound1.setPitch(id, 2);
						sound1.setLooping(id, false);
					}
				}

				ballShape.end();
				paddleShape.end();
				blockShape.end();
				backShape.end();
				frontShape.end();

				// Draw text over the pause menu for the start menu
				if (gameStartFlag == 1) {
					batch.begin();
					font.draw(batch, "Left Click to start game", (screenWidth / 100) * 35, (screenHeight / 100) * 58);
					batch.end();
				}

				// Draw text over the pause menu for the loss screen
				else if (gameLostFlag == 1) {
					batch.begin();
					font.draw(batch, "    YOU LOSE\n" +
										 "Press ESC to quit\n" +
							 			 "Left click to restart", (screenWidth / 100) * 39, (screenHeight / 100) * 65);
					batch.end();
				}

				// Draw text over the pause menu for the victory screen
				else if (gameWonFlag == 1) {
					batch.begin();
					font.draw(batch, "    YOU WIN\n" +
										 "Press ESC to quit\n" +
										 "Left click to restart", (screenWidth / 100) * 39, (screenHeight / 100) * 65);
					batch.end();
				}

				// if none of the other flags were on, it was because game was paused, display pause
				else {
					batch.begin();
					font.draw(batch, "PAUSED", (screenWidth / 100) * 45, (screenHeight / 100) * 58);
					batch.end();
				}

				break;

			case RUN:
				/* This is an example of using polling to check for input, currently using event handling instead inside the MyInputProcessor.java class
				pauseBtnPressed = Gdx.input.isKeyJustPressed(Input.Keys.P);
				if (pauseBtnPressed) {
					System.out.println("Using polling to check to pause game");
					this.setState(State.PAUSE);
				}
				 */

				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				ballShape.begin(ShapeRenderer.ShapeType.Filled);
				paddleShape.begin(ShapeRenderer.ShapeType.Filled);
				blockShape.begin(ShapeRenderer.ShapeType.Filled);
				backShape.begin(ShapeRenderer.ShapeType.Filled);
				frontShape.begin(ShapeRenderer.ShapeType.Filled);

				// set by menuSwitch(), if yes, draws the menu screen
				if (backBlock.isActive() && frontBlock.isActive()) {
					backBlock.setColor(Color.WHITE);
					frontBlock.setColor(Color.BLACK);
					backBlock.draw(backShape);
					frontBlock.draw(frontShape);
				}

				// get the timer to set the ball color back from green to white
				// the timer for how how many frames the ball stays green is in Ball.checkCollision()
				double temp = ball.getTimer();
				ball.setTimer(temp - 1);
				if (ball.getTimer() == 0) {
					ball.setColor(Color.WHITE);
				}

				ball.update();
				paddle.update();

				// check for if ball hit the bottom of the screen
				if (ball.isHitBottom()) {
					System.out.println("Ball isHitBottom() = true");
					menuSwitch(true);
					setState(State.PAUSE);
					gameLostFlag = 1;
				}

				// check for if all blocks have been removed from screen, draw victory screen
				if (blockCount <= 0) {
					System.out.println("Block count = " + blockCount);
					menuSwitch(true);
					setState(State.PAUSE);
					gameWonFlag = 1;
				}


				boolean ballHit = ball.checkCollision(paddle, paddleShape);

				if (ballHit) {
					sound1.play();
				}

				ball.draw(ballShape);
				paddle.draw(paddleShape);

				for (Block block : blocks) {
					block.draw(blockShape);
					ballHit = ball.checkCollision(block, blockShape);
					if (ballHit) {
						sound1.play();
					}
				}
				for (int i = 0; i < blocks.size(); i++) {
					Block block = blocks.get(i);
					if (!block.isActive()) {
						blocks.remove(block);
						blockCount--;
						i--;
					}
				}

				ballShape.end();
				paddleShape.end();
				blockShape.end();
				backShape.end();
				frontShape.end();
				break;

			case RESUME:
				break;

			case STOPPED:
				break;
		}

	}

	@Override
	public void dispose() {
		sound1.dispose();
		sound2.dispose();
		batch.dispose();
		font.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void menuSwitch(Boolean bool) {
		if (bool) {
			this.backBlock.setState(true);
			this.frontBlock.setState(true);
		}
		else {
			this.backBlock.setState(false);
			this.frontBlock.setState(false);
		}
	}
}