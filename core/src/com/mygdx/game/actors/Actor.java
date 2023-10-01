package com.mygdx.game.actors;


import com.badlogic.gdx.graphics.Color;

public interface Actor {
    public boolean isInBounds(int xCoordinate, int yCoordinate);
    public void setColor(Color color);
    public boolean isActive();
    public void setState(boolean bool);
}
