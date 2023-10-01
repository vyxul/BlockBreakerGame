package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle implements Actor {
    int x, y, width, height;
    int xMin, xMax, yMin, yMax;
    Color color;
    boolean active;

    public Paddle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xMin = x;
        this.xMax = x + width;
        this.yMin = y;
        this.yMax = y + height;
        active = true;
    }

    public void update() {
        int cursor = Gdx.input.getX();
        if ((cursor < (width / 2)) || (cursor > (Gdx.graphics.getWidth() - (width / 2))))
            return;

        x = Gdx.input.getX() - (width / 2);
//        y = Gdx.graphics.getHeight() - Gdx.input.getY() - (height / 2);

        this.xMin = x;
        this.xMax = x + width;
//        this.yMin = y;
//        this.yMax = y + height;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(this.color);
        shape.rect(x, y, width, height);
    }

    @Override
    public boolean isInBounds(int xCoordinate, int yCoordinate) {
        if ((xCoordinate >= xMin && xCoordinate <= xMax) && (yCoordinate >= yMin && yCoordinate <= yMax)) {
            return true;
        }
        return false;
    }

    public int ballSpeedChange(int xCoordinate, int yCoordinate) {
        int portion = width / 11;

        // find which section the ball hit the paddle in and return corresponding speed change
        if (xCoordinate >= (x + (portion * 0)) && xCoordinate < (x + (portion * 1)))
            return -5;

        if (xCoordinate >= (x + (portion * 1)) && xCoordinate < (x + (portion * 2)))
            return -4;

        if (xCoordinate >= (x + (portion * 2)) && xCoordinate < (x + (portion * 3)))
            return -3;

        if (xCoordinate >= (x + (portion * 3)) && xCoordinate < (x + (portion * 4)))
            return -2;

        if (xCoordinate >= (x + (portion * 4)) && xCoordinate < (x + (portion * 5)))
            return -1;

        if (xCoordinate >= (x + (portion * 5)) && xCoordinate < (x + (portion * 6)))
            return 0;

        if (xCoordinate >= (x + (portion * 6)) && xCoordinate < (x + (portion * 7)))
            return 1;

        if (xCoordinate >= (x + (portion * 7)) && xCoordinate < (x + (portion * 8)))
            return 2;

        if (xCoordinate >= (x + (portion * 8)) && xCoordinate < (x + (portion * 9)))
            return 3;

        if (xCoordinate >= (x + (portion * 9)) && xCoordinate < (x + (portion * 10)))
            return 4;

        if (xCoordinate >= (x + (portion * 10)) && xCoordinate <= (x + (portion * 11)))
            return 5;

        return 100;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setState(boolean bool) {
        active = bool;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Paddle{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                '}';
    }
}
