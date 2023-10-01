package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.actors.Block;

public class MyInputProcessor implements InputProcessor {
    char character;
    MyGdxGame myGdxGame;

    public MyInputProcessor(MyGdxGame game) {
        this.myGdxGame = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown() event, keycode = " + keycode);

        /*
         This is an example of using event handling to get input.
         Another way to get it done is with polling, which can be seen in MyGdxGame.java for the pause/unpause case, which is commented out.
         */
        // if pressing "ESC", fully closes game
        if (keycode == 111) {
            System.out.println("Escape button was pressed, exiting game now");

            Gdx.app.exit();
            return true;
        }

        // if pressing "P/p", pauses and unpauses game
        else if (keycode == 44) {
            // only allow pause/unpause if game hasn't been won or lost yet
            if (myGdxGame.gameWonFlag == 0 && myGdxGame.gameLostFlag == 0) {
                // check game state
                // if running, set to paused
                if (myGdxGame.getState() == State.RUN) {
                    System.out.println("Game was in RUN state, setting game state to PAUSE");
                    myGdxGame.menuSwitch(true);
                    myGdxGame.setState(State.PAUSE);
                }

                // if paused, set to run
                else if (myGdxGame.getState() == State.PAUSE) {
                    System.out.println("Game was in PAUSE state, setting game state to RUN");
                    myGdxGame.menuSwitch(false);
                    myGdxGame.ball.setColor(Color.WHITE);
                    myGdxGame.paddle.setColor(Color.WHITE);
                    for (Block block : myGdxGame.blocks) {
                        block.setColor(Color.WHITE);
                    }

                    myGdxGame.setState(State.RUN);
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp() event, keycode = " + keycode);

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped() event, character = " + character);
        this.character = character;

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("touchDown() event, screenX: %d, screenY: %d, pointer: %d, button: %d\n", screenX, screenY, pointer, button);
        if (button == 0) {
            if (myGdxGame.gameWonFlag == 1 || myGdxGame.gameLostFlag == 1) {
                myGdxGame.menuSwitch(false);
                myGdxGame.reset();
                myGdxGame.setState(State.RUN);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
