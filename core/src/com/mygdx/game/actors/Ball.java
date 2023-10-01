package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

public class Ball {
    int x, y, size;
    int xSpeed, ySpeed;

    int leftEdge, rightEdge, bottomEdge, topEdge;
    int topLeft, topRight, bottomLeft, bottomRight;
    Color color;
    boolean collisionPoint[] = new boolean[8];
    int collisionIndex;
    double timer;
    boolean hitBottom;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.leftEdge = x - size;
        this.rightEdge = x + size;
        this.bottomEdge = y - size;
        this.topEdge = y + size;
        this.timer = 0;
        this.hitBottom = false;

        // index 0 is top point, goes clockwise until index 7 at top left point
        for (int i = 0; i < collisionPoint.length; i++)
            collisionPoint[i] = false;

        collisionIndex = -1;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

        this.leftEdge = x - size;
        this.rightEdge = x + size;
        this.bottomEdge = y - size;
        this.topEdge = y + size;

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // if ball hits bottom edge of screen
        if (bottomEdge <= 0) {
            ySpeed = (ySpeed < 0) ? -ySpeed : ySpeed;
            System.out.println("Ball hit bottom edge of screen, in if statement, reversing ySpeed");
        }

        // if ball hits top edge of screen
        if (topEdge >= screenHeight)
            ySpeed = (ySpeed > 0) ? -ySpeed : ySpeed;

        // if ball hits left edge of screen
        if (leftEdge <= 0)
            xSpeed = (xSpeed < 0) ? -xSpeed : xSpeed;

        // if ball hits right edge of screen
        if (rightEdge >= screenWidth)
            xSpeed = (xSpeed > 0) ? -xSpeed : xSpeed;

        if (this.bottomEdge <= 0) {
            System.out.println("Ball hit bottom edge of screen, setting hitBottom to true");
            setHitBottom(true);
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(this.color);
        shape.circle(x, y, size);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getXSpeed() {
        return this.xSpeed;
    }
    public void setXSpeed(int num) {
        this.xSpeed = num;
    }

    public int getYSpeed() {
        return this.ySpeed;
    }
    public void setYSpeed(int num) {
        this.ySpeed = num;
    }

    public boolean checkCollision(Actor actor, ShapeRenderer shapeRenderer) {
        if (collidesWith(actor)) {
            this.setColor(Color.GREEN);
            //System.out.println("actor class name: " + actor.getClass().getName());
            //shapeRenderer.setColor(Color.RED);

            // if collision point is on top side (7, 0, 1), then make ySpeed negative
            // if collision point is on bot side (3, 4, 5), then make ySpeed positive
            int topIndexes[] = {0, 1, 7};
            int botIndexes[] = {3, 4, 5};
            int ySpeed = getYSpeed();
            for (int i : topIndexes) {
                if (collisionIndex == i) {
                    setYSpeed(ySpeed > 0 ? -ySpeed : ySpeed);
                }
            }
            for (int i : botIndexes) {
                if (collisionIndex == i) {
                    setYSpeed(ySpeed < 0 ? -ySpeed : ySpeed);
                }
            }

            if (actor.getClass().equals(Block.class)) {
                //System.out.println("checkCollision(), if block");
                actor.setState(false);

                // if collision point is on right side (1, 2, 3), make xSpeed negative
                // if collision point is on left side (5, 6, 7),  make xSpeed positive
                int rightIndexes[] = {1, 2, 3};
                int leftIndexes[] =  {5, 6, 7};
                int xSpeed = getXSpeed();
                for (int i : rightIndexes) {
                    if (collisionIndex == i) {
                        setXSpeed(xSpeed > 0 ? -xSpeed : xSpeed);
                    }
                }
                for (int i : leftIndexes) {
                    if (collisionIndex == i) {
                        setXSpeed(xSpeed < 0 ? -xSpeed : xSpeed);
                    }
                }
            }

            if (actor.getClass().equals(Paddle.class)) {
                int newSpeed = ((Paddle) actor).ballSpeedChange(x, bottomEdge);

                // if newSpeed = 100, the ball was hit on one of the bottom corners and need to change speed based on which corner was hit
                if (newSpeed == 100) {
                    if (collisionIndex == 3)
                        newSpeed = -10;
                    else if (collisionIndex == 5)
                        newSpeed = 10;
                    else
                        newSpeed = 0;
                }

                setXSpeed(newSpeed);

                this.setColor(Color.GREEN);
            }

            // reset the collisionPoint[] to all false and collisionIndex to -1
            for (int i = 0; i < collisionPoint.length; i++) {
                collisionPoint[i] = false;
            }
            collisionIndex = -1;

            // after how many frames should the ball change color back to white
            // default of 60 FPS
            this.setTimer(30);

            return true;
        }
        else {
            return false;
        }
    }

    private boolean collidesWith(Actor actor) {
    /*
     * Checks the collision points if any of those points collide with an actor's hitbox.
     * Goes from top point of ball (index 0) clockwise until top left point outside of ball (index 7)
     * Save it into array to know how to change the velocity of ball
     */
        collisionPoint[0] = actor.isInBounds(x, topEdge);
        collisionPoint[1] = actor.isInBounds(rightEdge, topEdge);
        collisionPoint[2] = actor.isInBounds(rightEdge, y);
        collisionPoint[3] = actor.isInBounds(rightEdge, bottomEdge);
        collisionPoint[4] = actor.isInBounds(x, bottomEdge);
        collisionPoint[5] = actor.isInBounds(leftEdge, bottomEdge);
        collisionPoint[6] = actor.isInBounds(leftEdge, y);
        collisionPoint[7] = actor.isInBounds(leftEdge, topEdge);

        for (int i = 0; i < collisionPoint.length; i++)
            if (collisionPoint[i]) {
                collisionIndex = i;
                return true;
            }

        return false;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public boolean isHitBottom() {
        return hitBottom;
    }

    public void setHitBottom(boolean hitBottom) {
        this.hitBottom = hitBottom;
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
        return "Ball{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + size +
                ", xSpeed=" + xSpeed +
                ", ySpeed=" + ySpeed +
                ", leftEdge=" + leftEdge +
                ", rightEdge=" + rightEdge +
                ", bottomEdge=" + bottomEdge +
                ", topEdge=" + topEdge +
                ", color=" + color +
                '}';
    }
}
