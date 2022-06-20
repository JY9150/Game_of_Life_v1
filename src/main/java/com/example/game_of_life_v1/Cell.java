package com.example.game_of_life_v1;

public class Cell {
    private boolean alive;

    Cell(boolean alive){
        this.alive = alive;
    }

    public boolean isAlive(){
        return alive;
    }

    public void setState(boolean alive){
        this.alive = alive;
    }

}
