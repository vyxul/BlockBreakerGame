package com.mygdx.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Block implements Actor {
    int x, y, width, height;
    int xMin, xMax, yMin, yMax;
    Color color;
    boolean active;

    public Block(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xMin = x;
        this.xMax = x + width;
        this.yMin = y;
        this.yMax = y + height;
        active = true;
        color = Color.WHITE;
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

    @Override
    public String toString() {
        return "Block{" +
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
